package io.openchat.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.openchat.data.mock.AppSnapshot
import io.openchat.data.mock.ChatMessage
import io.openchat.data.mock.ConversationMode
import io.openchat.data.mock.FakeStateController
import io.openchat.data.mock.ReasoningEffort
import io.openchat.data.mock.UiSimState
import io.openchat.navigation.OpenChatDestination
import io.openchat.ui.components.DsIcon
import io.openchat.ui.components.SectionHeader
import io.openchat.ui.components.StateSimulationHost
import io.openchat.ui.components.StatusBadge
import io.openchat.ui.theme.OpenChatDimens
import io.openchat.ui.theme.OpenChatIcons
import io.openchat.ui.theme.OpenChatPalette

@Composable
fun NavigationDrawerContent(
    snapshot: AppSnapshot,
    currentDestination: OpenChatDestination,
    controller: FakeStateController,
    onSelectDestination: (OpenChatDestination) -> Unit,
    onCloseDrawer: () -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .width(OpenChatDimens.DrawerContainerWidth)
            .fillMaxHeight(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(
            topEnd = OpenChatDimens.DrawerCornerSize,
            bottomEnd = OpenChatDimens.DrawerCornerSize,
        ),
        tonalElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search chats", style = MaterialTheme.typography.bodyMedium) },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                )
                IconButton(
                    onClick = {
                        controller.startNewChat()
                        onSelectDestination(OpenChatDestination.CONVERSATION)
                        onCloseDrawer()
                    },
                ) {
                    DsIcon(
                        drawableRes = OpenChatIcons.Pencil,
                        contentDescription = "New chat",
                        size = 22.dp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = {
                        controller.startNewChat(mode = ConversationMode.DEFAULT, isTemporary = false)
                        onSelectDestination(OpenChatDestination.CONVERSATION)
                        onCloseDrawer()
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    DsIcon(
                        drawableRes = OpenChatIcons.ChatBubble,
                        contentDescription = null,
                        size = 16.dp,
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("New Chat", style = MaterialTheme.typography.labelMedium)
                }
                OutlinedButton(
                    onClick = {
                        controller.startNewChat(mode = ConversationMode.TEMPORARY, isTemporary = true)
                        onSelectDestination(OpenChatDestination.CONVERSATION)
                        onCloseDrawer()
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Temporary", style = MaterialTheme.typography.labelMedium)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                item {
                    Text(
                        text = "SURFACES & HUBS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp),
                    )
                }

                items(OpenChatDestination.entries) { destination ->
                    val isVisible = when (destination) {
                        OpenChatDestination.CODEX_REMOTE -> snapshot.isFlagEnabled("feature_codex_remote_next")
                        OpenChatDestination.ORBIT_TIBRO -> snapshot.isFlagEnabled("feature_orbit_tibro_dots")
                        OpenChatDestination.CALPICO_SOCIAL -> snapshot.isFlagEnabled("feature_calpico_social_chat")
                        OpenChatDestination.ARTIFACT_LIBRARY -> snapshot.isFlagEnabled("feature_artifacts_sites_pages")
                        else -> true
                    }
                    if (isVisible) {
                        val selected = currentDestination == destination
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    onSelectDestination(destination)
                                    onCloseDrawer()
                                },
                            color = if (selected) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                Color.Transparent
                            },
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f),
                                ) {
                                    DsIcon(
                                        drawableRes = destination.iconRes,
                                        contentDescription = destination.title,
                                        size = 20.dp,
                                        tint = if (selected) {
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                        } else {
                                            MaterialTheme.colorScheme.onSurface
                                        },
                                    )
                                    Column {
                                        Text(
                                            text = destination.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                        Text(
                                            text = destination.deepLinkPath,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                }
                                StatusBadge(text = destination.phaseBadge)
                            }
                        }
                    }
                }

                item {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = "PINNED GPTS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp),
                    )
                }

                items(snapshot.gizmos.filter { it.isPinnedInDrawer }) { gizmo ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                controller.startNewChat(
                                    mode = ConversationMode.GIZMO,
                                    gizmoId = gizmo.id,
                                )
                                onSelectDestination(OpenChatDestination.CONVERSATION)
                                onCloseDrawer()
                            }
                            .padding(horizontal = 10.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        DsIcon(
                            drawableRes = OpenChatIcons.Braces,
                            contentDescription = null,
                            size = 18.dp,
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = gizmo.name,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = gizmo.author,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                item {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = "RECENT CHATS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp),
                    )
                }

                val filteredThreads = snapshot.conversations.filter { thread ->
                    (!thread.isLocked || snapshot.isLockedChatsUnlocked) &&
                        (searchQuery.isBlank() || thread.title.contains(searchQuery, ignoreCase = true))
                }

                items(filteredThreads) { thread ->
                    val isSelected = thread.id == snapshot.activeConversationId &&
                        currentDestination == OpenChatDestination.CONVERSATION
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                controller.selectConversation(thread.id)
                                onSelectDestination(OpenChatDestination.CONVERSATION)
                                onCloseDrawer()
                            },
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            Color.Transparent
                        },
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = thread.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    text = "${thread.category} | ${thread.updatedRelative} | ${thread.modelSlug}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            if (thread.isPinned) {
                                StatusBadge(text = "PIN")
                            } else if (thread.isWorkMode) {
                                StatusBadge(text = "WORK")
                            }
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        onSelectDestination(OpenChatDestination.SETTINGS_AND_PROFILE)
                        onCloseDrawer()
                    }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(OpenChatDimens.AvatarMedium)
                            .clip(CircleShape)
                            .background(OpenChatPalette.BrandPurple),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = snapshot.userFullName.take(2).uppercase(),
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Column {
                        Text(
                            text = snapshot.userFullName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = "@${snapshot.userUsername} | ${snapshot.subscriptionTier.label}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                StatusBadge(text = snapshot.subscriptionTier.badge)
            }
        }
    }
}

@Composable
fun ConversationScreen(
    snapshot: AppSnapshot,
    controller: FakeStateController,
    onNavigate: (OpenChatDestination) -> Unit,
) {
    var promptInput by remember { mutableStateOf("") }
    var showModelSheet by remember { mutableStateOf(false) }
    val activeThread = snapshot.activeConversation
    val selectedPet = snapshot.codexPets.find { it.id == snapshot.selectedPetId }

    Column(modifier = Modifier.fillMaxSize()) {
        // Model, Effort, Work Mode & Companion Pet Control Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedButton(onClick = { showModelSheet = !showModelSheet }) {
                        DsIcon(
                            drawableRes = OpenChatIcons.Emblem,
                            contentDescription = null,
                            size = 16.dp,
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Model: ${snapshot.selectedModelSlug}",
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }

                    if (snapshot.isFlagEnabled("feature_reasoning_effort_gauge")) {
                        FilterChip(
                            selected = true,
                            onClick = {
                                val nextIndex = (ReasoningEffort.entries.indexOf(snapshot.selectedEffort) + 1) %
                                    ReasoningEffort.entries.size
                                controller.selectEffort(ReasoningEffort.entries[nextIndex])
                            },
                            label = {
                                Text(
                                    text = "${snapshot.selectedEffort.label} effort",
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            },
                        )
                    }

                    FilterChip(
                        selected = snapshot.isWorkMode,
                        onClick = { controller.toggleWorkMode() },
                        label = {
                            Text(
                                text = if (snapshot.isWorkMode) "Work Mode: ON" else "Use Work",
                                style = MaterialTheme.typography.labelSmall,
                            )
                        },
                    )

                    if (snapshot.isFlagEnabled("feature_codex_companion_pets") && selectedPet != null) {
                        StatusBadge(
                            text = "${selectedPet.asciiPreview} ${selectedPet.name}",
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }

                    if (snapshot.isLockdownModeEnabled) {
                        StatusBadge(
                            text = "LOCKDOWN ON",
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        )
                    }
                }

                AnimatedVisibility(visible = showModelSheet) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Text(
                                text = "Select model & reasoning effort",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                            )
                            snapshot.models.forEach { model ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            controller.selectModel(model.slug)
                                            showModelSheet = false
                                        }
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = model.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium,
                                        )
                                        Text(
                                            text = model.subtitle,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                    StatusBadge(text = model.badge)
                                }
                            }
                        }
                    }
                }
            }
        }

        StateSimulationHost(
            state = snapshot.uiSimState,
            loadingTitle = "Refreshing conversation...",
            emptyTitle = "No recent chats",
            emptySubtitle = "Bring your ideas to life and get things done. Choose a starter prompt below.",
            emptyActionLabel = "Start sample prompt",
            onEmptyAction = {
                controller.setUiSimState(UiSimState.SUCCESS)
                controller.sendUserMessage("Explain spatial spring motion tokens in Material 3")
            },
            errorTitle = "Couldn't start this chat",
            errorSubtitle = "Network connection lost. Attempting to reconnect...",
            onRetry = { controller.setUiSimState(UiSimState.SUCCESS) },
            modifier = Modifier.weight(1f),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                if (snapshot.isTemporaryChat) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Temporary chat",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Text(
                                    text = "Temporary chats won't appear in your history and won't be used to improve our models.",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                    }
                }

                if (activeThread?.branchedFromTitle != null) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            DsIcon(
                                drawableRes = OpenChatIcons.ArrowCurvedRight,
                                contentDescription = null,
                                size = 16.dp,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                            Text(
                                text = "Branched from ${activeThread.branchedFromTitle}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }

                if (activeThread == null || activeThread.messages.isEmpty()) {
                    item {
                        EmptyConversationSuggestions(
                            onSelectPrompt = { prompt -> controller.sendUserMessage(prompt) },
                            onNavigate = onNavigate,
                        )
                    }
                } else {
                    items(activeThread.messages, key = { it.id }) { message ->
                        MessageBubbleCard(
                            message = message,
                            variantsFlagEnabled = snapshot.isFlagEnabled("feature_variants_in_stream"),
                            onSelectVariant = { idx -> controller.selectMessageVariant(message.id, idx) },
                        )
                    }
                }

                if (snapshot.subscriptionTier == io.openchat.data.mock.SubscriptionTier.FREE &&
                    snapshot.isFlagEnabled("feature_ads_transparency_controls")
                ) {
                    item {
                        SponsoredAdCard(
                            onUpgradeClick = { onNavigate(OpenChatDestination.AUTH_AND_SUBSCRIPTION) },
                            onManageAdsClick = { onNavigate(OpenChatDestination.SETTINGS_AND_PROFILE) },
                        )
                    }
                }

                item {
                    Text(
                        text = "Open Chat can make mistakes, so check anything important.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                    )
                }
            }
        }

        // Composer Bar
        ComposerInputBar(
            promptInput = promptInput,
            onPromptChange = { promptInput = it },
            isWebSearchEnabled = snapshot.isWebSearchEnabled,
            isDeepResearchEnabled = snapshot.isDeepResearchEnabled,
            onToggleWebSearch = { controller.toggleWebSearch() },
            onToggleDeepResearch = { controller.toggleDeepResearch() },
            onSend = {
                controller.sendUserMessage(promptInput)
                promptInput = ""
            },
            onOpenCameraSketch = { onNavigate(OpenChatDestination.CAMERA_SKETCH) },
            onOpenVoiceCall = { onNavigate(OpenChatDestination.VOICE_CALL) },
            onOpenLibrary = { onNavigate(OpenChatDestination.ARTIFACT_LIBRARY) },
        )
    }
}

@Composable
private fun EmptyConversationSuggestions(
    onSelectPrompt: (String) -> Unit,
    onNavigate: (OpenChatDestination) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            text = "Welcome to Open Chat",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = "Bring your ideas to life and get things done.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        val quickPrompts = listOf(
            "Let's create" to "Create an interactive solar telemetry dashboard specification",
            "Study and Learn" to "Solve 3(2x-5)=4x+8 with step-by-step guided hints",
            "Code & Architecture" to "Audit a Jetpack Compose screen for unidirectional state flow",
            "Schedule task" to "/goal Create a daily 8:00 AM GitHub Actions digest",
        )

        quickPrompts.forEach { (category, prompt) ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(OpenChatDimens.CardCornerRadius))
                    .clickable { onSelectPrompt(prompt) },
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = prompt,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedButton(onClick = { onNavigate(OpenChatDestination.CAMERA_SKETCH) }) {
                DsIcon(drawableRes = OpenChatIcons.PaintbrushOnPencil, contentDescription = null, size = 16.dp)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Create an image")
            }
            OutlinedButton(onClick = { onNavigate(OpenChatDestination.VOICE_CALL) }) {
                DsIcon(drawableRes = OpenChatIcons.VoiceButton, contentDescription = null, size = 16.dp)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Say hello to Voice")
            }
            OutlinedButton(onClick = { onNavigate(OpenChatDestination.CODEX_REMOTE) }) {
                DsIcon(drawableRes = OpenChatIcons.Codex, contentDescription = null, size = 16.dp)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Build with Codex")
            }
        }
    }
}

@Composable
private fun MessageBubbleCard(
    message: ChatMessage,
    variantsFlagEnabled: Boolean,
    onSelectVariant: (Int) -> Unit,
) {
    var showReasoning by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start,
    ) {
        Surface(
            shape = RoundedCornerShape(OpenChatDimens.MessageBubbleCornerRadius),
            color = if (message.isUser) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
            },
            modifier = Modifier.fillMaxWidth(if (message.isUser) 0.88f else 1f),
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        DsIcon(
                            drawableRes = if (message.isUser) OpenChatIcons.Personalization else OpenChatIcons.Emblem,
                            contentDescription = null,
                            size = 16.dp,
                        )
                        Text(
                            text = message.senderName,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                        if (!message.isUser) {
                            StatusBadge(text = message.modelSlug)
                        }
                    }
                    Text(
                        text = message.timestamp,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                // Reasoning / Thinking accordion (`Thought for %ds`)
                if (message.thinkingSeconds > 0 && message.reasoningSteps.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showReasoning = !showReasoning },
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    DsIcon(
                                        drawableRes = OpenChatIcons.Brain,
                                        contentDescription = null,
                                        size = 16.dp,
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                    Text(
                                        text = "Thought for ${message.thinkingSeconds}s",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Medium,
                                    )
                                }
                                Text(
                                    text = if (showReasoning) "Collapse" else "Expand",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                            AnimatedVisibility(visible = showReasoning) {
                                Column(
                                    modifier = Modifier.padding(top = 8.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    message.reasoningSteps.forEach { step ->
                                        Text(
                                            text = "* $step",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Tool call execution pills
                if (message.toolSteps.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        message.toolSteps.forEach { tool ->
                            StatusBadge(
                                text = "${tool.toolName} | ${tool.durationMs} ms | ${tool.summary}",
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }

                // Message text
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyLarge,
                )

                // Code Blocks (`Code`, `Preview`, `Copy to Clipboard`, `Code Output`)
                message.codeSnippets.forEach { snippet ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = OpenChatPalette.Neutral10,
                        contentColor = OpenChatPalette.Neutral95,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "${snippet.filename} (${snippet.language})",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = OpenChatPalette.BrandSecondary,
                                )
                                Text(
                                    text = "Copy to Clipboard",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = OpenChatPalette.Neutral87,
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = snippet.code,
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                                color = OpenChatPalette.Neutral95,
                            )
                            if (snippet.output != null) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    color = OpenChatPalette.Neutral24,
                                )
                                Text(
                                    text = "Code Output: ${snippet.output}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                                    color = OpenChatPalette.CallAnswerGreen,
                                )
                            }
                        }
                    }
                }

                // Citations (`Sources`, `Web sources`)
                if (message.citations.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Sources",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        message.citations.forEach { citation ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = "${citation.title} (${citation.domain})",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                    Text(
                                        text = citation.snippet,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }
                    }
                }

                // Variants In Stream (`defpackage/pov.java`: `See option %1$d`, `I prefer this version`)
                if (variantsFlagEnabled && message.variants.size > 1) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        text = "Variants In Stream | See another version",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        message.variants.forEachIndexed { idx, variant ->
                            FilterChip(
                                selected = message.selectedVariantIndex == idx,
                                onClick = { onSelectVariant(idx) },
                                label = {
                                    Text(
                                        text = variant.label,
                                        style = MaterialTheme.typography.labelSmall,
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SponsoredAdCard(
    onUpgradeClick: () -> Unit,
    onManageAdsClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp),
            ),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StatusBadge(text = "Sponsored")
                Text(
                    text = "Ads do not influence the answers you get from Open Chat.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = "Go ad-free with Open Chat Plus",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "Ads allow for more messages on the Free plan. Upgrade to Plus for full access without ads.",
                style = MaterialTheme.typography.bodySmall,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onUpgradeClick) {
                    Text("Upgrade to Plus", style = MaterialTheme.typography.labelSmall)
                }
                OutlinedButton(onClick = onManageAdsClick) {
                    Text("Ads controls", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
private fun ComposerInputBar(
    promptInput: String,
    onPromptChange: (String) -> Unit,
    isWebSearchEnabled: Boolean,
    isDeepResearchEnabled: Boolean,
    onToggleWebSearch: () -> Unit,
    onToggleDeepResearch: () -> Unit,
    onSend: () -> Unit,
    onOpenCameraSketch: () -> Unit,
    onOpenVoiceCall: () -> Unit,
    onOpenLibrary: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            // Tool & Slash Command Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FilterChip(
                    selected = isWebSearchEnabled,
                    onClick = onToggleWebSearch,
                    label = { Text("Web search", style = MaterialTheme.typography.labelSmall) },
                )
                FilterChip(
                    selected = isDeepResearchEnabled,
                    onClick = onToggleDeepResearch,
                    label = { Text("Deep Research", style = MaterialTheme.typography.labelSmall) },
                )
                FilterChip(
                    selected = false,
                    onClick = { onPromptChange("/goal ") },
                    label = { Text("/goal", style = MaterialTheme.typography.labelSmall) },
                )
                FilterChip(
                    selected = false,
                    onClick = { onPromptChange("/plan ") },
                    label = { Text("/plan", style = MaterialTheme.typography.labelSmall) },
                )
                FilterChip(
                    selected = false,
                    onClick = onOpenCameraSketch,
                    label = { Text("Camera & Sketch", style = MaterialTheme.typography.labelSmall) },
                )
                FilterChip(
                    selected = false,
                    onClick = onOpenLibrary,
                    label = { Text("Add files", style = MaterialTheme.typography.labelSmall) },
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                IconButton(onClick = onOpenCameraSketch) {
                    DsIcon(
                        drawableRes = OpenChatIcons.Camera,
                        contentDescription = "Camera",
                        size = 22.dp,
                    )
                }
                OutlinedTextField(
                    value = promptInput,
                    onValueChange = onPromptChange,
                    placeholder = { Text("Ask anything") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(OpenChatDimens.ComposerCornerRadius),
                    maxLines = 4,
                )
                IconButton(
                    onClick = {
                        if (promptInput.isBlank()) {
                            onPromptChange("Dictated voice note: Summarize today's key action items.")
                        } else {
                            onSend()
                        }
                    },
                ) {
                    DsIcon(
                        drawableRes = OpenChatIcons.Microphone,
                        contentDescription = "Use your voice",
                        size = 22.dp,
                    )
                }
                Button(
                    onClick = {
                        if (promptInput.isBlank()) {
                            onOpenVoiceCall()
                        } else {
                            onSend()
                        }
                    },
                    shape = CircleShape,
                ) {
                    DsIcon(
                        drawableRes = if (promptInput.isBlank()) {
                            OpenChatIcons.VoiceButton
                        } else {
                            OpenChatIcons.PlaneUpRight
                        },
                        contentDescription = if (promptInput.isBlank()) "Start a voice conversation" else "Send message",
                        size = 18.dp,
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}

@Composable
fun VoiceCallScreen(
    snapshot: AppSnapshot,
    controller: FakeStateController,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "VoiceOrbPulse")
    val orbScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "OrbScaleAnimation",
    )

    val currentVoice = snapshot.voicePresets.find { it.id == snapshot.selectedVoiceId }
        ?: snapshot.voicePresets.first()

    StateSimulationHost(
        state = snapshot.uiSimState,
        loadingTitle = "Connecting to Realtime Voice...",
        emptyTitle = "Voice is currently unavailable",
        emptySubtitle = "Enable microphone access or select a voice preset below to start talking.",
        errorTitle = "Voice couldn't connect. Please try again.",
        errorSubtitle = "Voice connection was interrupted. Try again.",
        onRetry = { controller.setUiSimState(UiSimState.SUCCESS) },
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "Open Chat Voice",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Voice: ${currentVoice.name} | ${if (snapshot.isVoiceMuted) "Muted" else "Listening"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                OutlinedButton(onClick = { controller.toggleVoiceDebugOverlay() }) {
                    Text(
                        text = if (snapshot.showVoiceDebugOverlay) "Hide Voice Debug" else "Voice Debug",
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Animated Spatial Voice Orb
            Box(
                modifier = Modifier
                    .size(OpenChatDimens.VoiceOrbSize)
                    .scale(if (snapshot.isVoiceMuted) 1f else orbScale)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = if (snapshot.isVoiceMuted) {
                                listOf(OpenChatPalette.GrayButton, OpenChatPalette.Neutral17)
                            } else {
                                listOf(
                                    OpenChatPalette.BrandSecondary,
                                    OpenChatPalette.BrandPurple,
                                    OpenChatPalette.BrandPurpleVariant,
                                )
                            },
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    DsIcon(
                        drawableRes = if (snapshot.isVoiceMuted) {
                            OpenChatIcons.MicrophoneSlashFill
                        } else {
                            OpenChatIcons.VoiceButton
                        },
                        contentDescription = "Voice status",
                        size = 42.dp,
                        tint = Color.White,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (snapshot.isVoiceMuted) "Muted" else "Listening",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Text(
                text = if (snapshot.isVoiceCameraLive) {
                    "Live camera active | Chat about your surroundings. Ask before recording others."
                } else {
                    "Say what's on your mind. Open Chat listens, responds, and keeps the conversation flowing naturally."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            // Primary Call Controls (`Mute`, `Live camera`, `End call`)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = { controller.toggleVoiceMute() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (snapshot.isVoiceMuted) {
                            OpenChatPalette.CallDeclineRed
                        } else {
                            MaterialTheme.colorScheme.secondaryContainer
                        },
                        contentColor = if (snapshot.isVoiceMuted) {
                            Color.White
                        } else {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        },
                    ),
                ) {
                    DsIcon(
                        drawableRes = if (snapshot.isVoiceMuted) {
                            OpenChatIcons.MicrophoneSlash
                        } else {
                            OpenChatIcons.Microphone
                        },
                        contentDescription = null,
                        size = 18.dp,
                        tint = if (snapshot.isVoiceMuted) Color.White else MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (snapshot.isVoiceMuted) "Unmute" else "Mute")
                }

                OutlinedButton(onClick = { controller.toggleVoiceCamera() }) {
                    DsIcon(drawableRes = OpenChatIcons.Camera, contentDescription = null, size = 18.dp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (snapshot.isVoiceCameraLive) "Stop Camera" else "Live camera")
                }
            }

            // Voice Debug Panel (`strings.xml#L7778-L7801`)
            AnimatedVisibility(visible = snapshot.showVoiceDebugOverlay) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = OpenChatPalette.Neutral10,
                    contentColor = OpenChatPalette.Neutral95,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "Voice Debug Telemetry (OrbitVoiceCallActivity)",
                            style = MaterialTheme.typography.titleSmall,
                            color = OpenChatPalette.BrandSecondary,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text("Voice Session ID: vsess_94a8bc01e2", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
                        Text("Connection state: Connected (WebRTC Opus 48kHz)", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
                        Text("Model Slug: ${snapshot.selectedModelSlug}-realtime", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
                        Text("RTT quality: 24 ms (Optimal)", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
                        Text("Concealment quality: 99.9%", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
                        Text("Wingman ducking: Enabled | Preconnect Applied: true", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
                    }
                }
            }

            HorizontalDivider()

            // Choose a Voice Picker (`defpackage/opv.java`)
            SectionHeader(
                title = "Choose a voice",
                subtitle = "Natural conversations that sense and respond to interruptions and humor.",
            )

            snapshot.voicePresets.forEach { preset ->
                val isSelected = preset.id == snapshot.selectedVoiceId
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { controller.selectVoicePreset(preset.id) },
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                    },
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = preset.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = preset.tagline,
                                style = MaterialTheme.typography.bodySmall,
                            )
                            Text(
                                text = preset.toneDescription,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        if (isSelected) {
                            StatusBadge(text = "Selected")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CameraSketchScreen(
    snapshot: AppSnapshot,
    controller: FakeStateController,
    onNavigateBackToChat: () -> Unit,
) {
    var selectedAspectRatio by remember { mutableStateOf("Square 1:1") }
    var selectedToolMode by remember { mutableStateOf("Draw and annotate") }
    var flashMode by remember { mutableStateOf("Flash auto") }
    var imagePrompt by remember { mutableStateOf("Isometric glass terrarium with bioluminescent moss") }
    val strokePoints = remember { mutableStateListOf<Pair<Offset, Offset>>() }

    val aspectRatios = listOf(
        "Square 1:1",
        "Landscape 4:3",
        "Portrait 3:4",
        "Widescreen 16:9",
        "Story 9:16",
    )

    val sketchTools = listOf(
        "Draw and annotate",
        "Select area",
        "Remove area",
        "Remove the background",
        "Clean up image",
        "Create stickers",
    )

    StateSimulationHost(
        state = snapshot.uiSimState,
        loadingTitle = "Preparing your image...",
        emptyTitle = "No images yet",
        emptySubtitle = "Take a photo, draw on the sketch canvas, or generate an image from a prompt.",
        errorTitle = "Unable to capture image",
        errorSubtitle = "The camera is unavailable. Close other apps using the camera, then try again.",
        onRetry = { controller.setUiSimState(UiSimState.SUCCESS) },
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            SectionHeader(
                title = "Camera, Sketch & Image Editor",
                subtitle = "Reconstructed from Camera (s3v), Sketch (l3v), and ImageEditActivity",
                actionLabel = flashMode,
                onActionClick = {
                    flashMode = when (flashMode) {
                        "Flash auto" -> "Flash on"
                        "Flash on" -> "Flash off"
                        else -> "Flash auto"
                    }
                },
            )

            // Aspect Ratio Selector (`strings.xml#L3575-L3579`)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                aspectRatios.forEach { ratio ->
                    FilterChip(
                        selected = selectedAspectRatio == ratio,
                        onClick = { selectedAspectRatio = ratio },
                        label = { Text(ratio, style = MaterialTheme.typography.labelSmall) },
                    )
                }
            }

            // Sketch & Markup Tool Selector (`strings.xml#L2342-L2349`, `L3573-L3574`)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                sketchTools.forEach { tool ->
                    FilterChip(
                        selected = selectedToolMode == tool,
                        onClick = { selectedToolMode = tool },
                        label = { Text(tool, style = MaterialTheme.typography.labelSmall) },
                    )
                }
            }

            // Interactive Sketch Canvas
            Surface(
                shape = RoundedCornerShape(OpenChatDimens.CardCornerRadius),
                color = OpenChatPalette.Pi2DefaultBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(OpenChatDimens.CardCornerRadius),
                    ),
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    val end = change.position
                                    val start = end - dragAmount
                                    strokePoints.add(start to end)
                                }
                            },
                    ) {
                        strokePoints.forEach { (start, end) ->
                            drawLine(
                                color = OpenChatPalette.BrandSecondary,
                                start = start,
                                end = end,
                                strokeWidth = 8f,
                                cap = StrokeCap.Round,
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp),
                    ) {
                        StatusBadge(text = "$selectedAspectRatio | $selectedToolMode")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Annotate by drawing on the image (${strokePoints.size} strokes)",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.78f),
                        )
                    }

                    OutlinedButton(
                        onClick = { strokePoints.clear() },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp),
                    ) {
                        Text("Clear", color = Color.White)
                    }
                }
            }

            OutlinedTextField(
                value = imagePrompt,
                onValueChange = { imagePrompt = it },
                label = { Text("Describe edits or image prompt") },
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = {
                        controller.sendUserMessage(
                            prompt = "[$selectedToolMode | $selectedAspectRatio] $imagePrompt",
                            attachmentTitle = "Sketch_${selectedAspectRatio.replace(" ", "_")}.png",
                        )
                        onNavigateBackToChat()
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    DsIcon(
                        drawableRes = OpenChatIcons.PaintPalette,
                        contentDescription = null,
                        size = 18.dp,
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Send to chat")
                }

                OutlinedButton(
                    onClick = {
                        controller.sendUserMessage(
                            prompt = "Create stickers from prompt: $imagePrompt",
                            attachmentTitle = "OpenChat_Stickers_Pack.webp",
                        )
                        onNavigateBackToChat()
                    },
                ) {
                    Text("Add to WhatsApp")
                }
            }
        }
    }
}
