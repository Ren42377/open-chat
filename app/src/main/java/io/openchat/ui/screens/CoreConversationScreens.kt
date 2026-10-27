package io.openchat.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.openchat.data.mock.AppSnapshot
import io.openchat.data.mock.ChatMessage
import io.openchat.data.mock.ConversationMode
import io.openchat.data.mock.ConversationThread
import io.openchat.data.mock.FakeStateController
import io.openchat.data.mock.ReasoningEffort
import io.openchat.data.mock.SubscriptionTier
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
    var showMoreFeatures by remember { mutableStateOf(false) }
    var threadMenuOpenId by remember { mutableStateOf<String?>(null) }

    val isDark = MaterialTheme.colorScheme.background == OpenChatPalette.DarkBgPrimary
    val drawerBackground = if (isDark) OpenChatPalette.DarkBgDrawer else OpenChatPalette.LightBgSecondary
    val selectedRowColor = if (isDark) OpenChatPalette.DarkBgTertiary else OpenChatPalette.LightBorderDefault

    Surface(
        modifier = Modifier
            .width(316.dp)
            .fillMaxHeight(),
        color = drawerBackground,
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(horizontal = 12.dp, vertical = 12.dp),
        ) {
            // Top Search Pill + Compose New Chat Icon Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(21.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        DsIcon(
                            drawableRes = OpenChatIcons.Search,
                            contentDescription = "Search",
                            size = 18.dp,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "Search",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                singleLine = true,
                                textStyle = TextStyle(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 15.sp,
                                ),
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { searchQuery = "" },
                                modifier = Modifier.size(20.dp),
                            ) {
                                DsIcon(
                                    drawableRes = OpenChatIcons.CloseX,
                                    contentDescription = "Clear search",
                                    size = 14.dp,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }

                IconButton(
                    onClick = {
                        controller.startNewChat(mode = ConversationMode.DEFAULT, isTemporary = false)
                        onSelectDestination(OpenChatDestination.CONVERSATION)
                        onCloseDrawer()
                    },
                ) {
                    DsIcon(
                        drawableRes = OpenChatIcons.ComposeNewChat,
                        contentDescription = "New chat",
                        size = 22.dp,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                // Primary Top Items (authentic ChatGPT Android sidebar rows)
                item {
                    DrawerMenuRow(
                        iconRes = OpenChatIcons.Emblem,
                        label = "ChatGPT",
                        selected = currentDestination == OpenChatDestination.CONVERSATION &&
                            snapshot.activeConversation?.messages?.isEmpty() == true,
                        selectedColor = selectedRowColor,
                        onClick = {
                            controller.startNewChat(mode = ConversationMode.DEFAULT, isTemporary = false)
                            onSelectDestination(OpenChatDestination.CONVERSATION)
                            onCloseDrawer()
                        },
                    )
                }

                if (snapshot.isFlagEnabled("feature_artifacts_sites_pages")) {
                    item {
                        DrawerMenuRow(
                            iconRes = OpenChatIcons.PhotoTiltedSquare,
                            label = "Library",
                            selected = currentDestination == OpenChatDestination.ARTIFACT_LIBRARY,
                            selectedColor = selectedRowColor,
                            onClick = {
                                onSelectDestination(OpenChatDestination.ARTIFACT_LIBRARY)
                                onCloseDrawer()
                            },
                        )
                    }
                }

                item {
                    DrawerMenuRow(
                        iconRes = OpenChatIcons.CircleGrid2x2,
                        label = "Explore GPTs",
                        selected = currentDestination == OpenChatDestination.DISCOVERY_HUB,
                        selectedColor = selectedRowColor,
                        onClick = {
                            onSelectDestination(OpenChatDestination.DISCOVERY_HUB)
                            onCloseDrawer()
                        },
                    )
                }

                if (snapshot.isFlagEnabled("feature_codex_remote_next")) {
                    item {
                        DrawerMenuRow(
                            iconRes = OpenChatIcons.Codex,
                            label = "Codex",
                            selected = currentDestination == OpenChatDestination.CODEX_REMOTE,
                            selectedColor = selectedRowColor,
                            onClick = {
                                onSelectDestination(OpenChatDestination.CODEX_REMOTE)
                                onCloseDrawer()
                            },
                        )
                    }
                }

                if (snapshot.isFlagEnabled("feature_orbit_tibro_dots")) {
                    item {
                        DrawerMenuRow(
                            iconRes = OpenChatIcons.Sparkles,
                            label = "Dots",
                            selected = currentDestination == OpenChatDestination.ORBIT_TIBRO,
                            selectedColor = selectedRowColor,
                            onClick = {
                                onSelectDestination(OpenChatDestination.ORBIT_TIBRO)
                                onCloseDrawer()
                            },
                        )
                    }
                }

                // Pinned Custom GPTs
                val pinnedGizmos = snapshot.gizmos.filter { it.isPinnedInDrawer }
                if (pinnedGizmos.isNotEmpty()) {
                    items(pinnedGizmos, key = { "gizmo-${it.id}" }) { gizmo ->
                        DrawerMenuRow(
                            iconRes = OpenChatIcons.Braces,
                            label = gizmo.name,
                            selected = false,
                            selectedColor = selectedRowColor,
                            onClick = {
                                controller.startNewChat(
                                    mode = ConversationMode.GIZMO,
                                    gizmoId = gizmo.id,
                                )
                                onSelectDestination(OpenChatDestination.CONVERSATION)
                                onCloseDrawer()
                            },
                        )
                    }
                }

                // Projects (Snorlax Spaces) Section
                if (snapshot.isFlagEnabled("feature_snorlax_projects_spaces")) {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Projects",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium,
                            )
                            IconButton(
                                onClick = {
                                    onSelectDestination(OpenChatDestination.SPACES_AND_TASKS)
                                    onCloseDrawer()
                                },
                                modifier = Modifier.size(24.dp),
                            ) {
                                DsIcon(
                                    drawableRes = OpenChatIcons.FolderPlus,
                                    contentDescription = "New project",
                                    size = 16.dp,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }

                    items(snapshot.projectSpaces, key = { "space-${it.id}" }) { space ->
                        DrawerMenuRow(
                            iconRes = OpenChatIcons.Folder,
                            label = space.name,
                            selected = currentDestination == OpenChatDestination.SPACES_AND_TASKS,
                            selectedColor = selectedRowColor,
                            onClick = {
                                onSelectDestination(OpenChatDestination.SPACES_AND_TASKS)
                                onCloseDrawer()
                            },
                        )
                    }
                }

                // Conversation History Grouped by Pinned, Today, Yesterday
                val visibleThreads = snapshot.conversations.filter { thread ->
                    !thread.isArchived &&
                        (!thread.isLocked || snapshot.isLockedChatsUnlocked) &&
                        (searchQuery.isBlank() || thread.title.contains(searchQuery, ignoreCase = true))
                }

                val pinnedThreads = visibleThreads.filter { it.isPinned }
                val todayThreads = visibleThreads.filter { !it.isPinned && it.category == "Today" }
                val otherThreads = visibleThreads.filter { !it.isPinned && it.category != "Today" }

                if (pinnedThreads.isNotEmpty()) {
                    item {
                        DrawerSectionHeader("Pinned")
                    }
                    items(pinnedThreads, key = { "pin-${it.id}" }) { thread ->
                        DrawerThreadRow(
                            thread = thread,
                            isSelected = thread.id == snapshot.activeConversationId &&
                                currentDestination == OpenChatDestination.CONVERSATION,
                            selectedColor = selectedRowColor,
                            menuExpanded = threadMenuOpenId == thread.id,
                            onOpenMenu = { threadMenuOpenId = thread.id },
                            onDismissMenu = { threadMenuOpenId = null },
                            onSelect = {
                                controller.selectConversation(thread.id)
                                onSelectDestination(OpenChatDestination.CONVERSATION)
                                onCloseDrawer()
                            },
                            onTogglePin = { controller.togglePinConversation(thread.id) },
                            onToggleArchive = { controller.toggleArchiveConversation(thread.id) },
                            onToggleLock = { controller.toggleLockConversation(thread.id) },
                            onDelete = { controller.deleteConversation(thread.id) },
                        )
                    }
                }

                if (todayThreads.isNotEmpty()) {
                    item {
                        DrawerSectionHeader("Today")
                    }
                    items(todayThreads, key = { "today-${it.id}" }) { thread ->
                        DrawerThreadRow(
                            thread = thread,
                            isSelected = thread.id == snapshot.activeConversationId &&
                                currentDestination == OpenChatDestination.CONVERSATION,
                            selectedColor = selectedRowColor,
                            menuExpanded = threadMenuOpenId == thread.id,
                            onOpenMenu = { threadMenuOpenId = thread.id },
                            onDismissMenu = { threadMenuOpenId = null },
                            onSelect = {
                                controller.selectConversation(thread.id)
                                onSelectDestination(OpenChatDestination.CONVERSATION)
                                onCloseDrawer()
                            },
                            onTogglePin = { controller.togglePinConversation(thread.id) },
                            onToggleArchive = { controller.toggleArchiveConversation(thread.id) },
                            onToggleLock = { controller.toggleLockConversation(thread.id) },
                            onDelete = { controller.deleteConversation(thread.id) },
                        )
                    }
                }

                if (otherThreads.isNotEmpty()) {
                    item {
                        DrawerSectionHeader("Yesterday")
                    }
                    items(otherThreads, key = { "other-${it.id}" }) { thread ->
                        DrawerThreadRow(
                            thread = thread,
                            isSelected = thread.id == snapshot.activeConversationId &&
                                currentDestination == OpenChatDestination.CONVERSATION,
                            selectedColor = selectedRowColor,
                            menuExpanded = threadMenuOpenId == thread.id,
                            onOpenMenu = { threadMenuOpenId = thread.id },
                            onDismissMenu = { threadMenuOpenId = null },
                            onSelect = {
                                controller.selectConversation(thread.id)
                                onSelectDestination(OpenChatDestination.CONVERSATION)
                                onCloseDrawer()
                            },
                            onTogglePin = { controller.togglePinConversation(thread.id) },
                            onToggleArchive = { controller.toggleArchiveConversation(thread.id) },
                            onToggleLock = { controller.toggleLockConversation(thread.id) },
                            onDelete = { controller.deleteConversation(thread.id) },
                        )
                    }
                }

                // Expandable "More" Section for Verticals, Social, Locked Chats & Inspector
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { showMoreFeatures = !showMoreFeatures },
                        color = Color.Transparent,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = "More",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium,
                            )
                            DsIcon(
                                drawableRes = if (showMoreFeatures) {
                                    OpenChatIcons.ChevronDown
                                } else {
                                    OpenChatIcons.ChevronRight
                                },
                                contentDescription = "Toggle more surfaces",
                                size = 16.dp,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                if (showMoreFeatures) {
                    item {
                        DrawerMenuRow(
                            iconRes = OpenChatIcons.Calendar,
                            label = "Scheduled tasks",
                            selected = currentDestination == OpenChatDestination.SPACES_AND_TASKS,
                            selectedColor = selectedRowColor,
                            onClick = {
                                onSelectDestination(OpenChatDestination.SPACES_AND_TASKS)
                                onCloseDrawer()
                            },
                        )
                    }
                    if (snapshot.isFlagEnabled("feature_calpico_social_chat")) {
                        item {
                            DrawerMenuRow(
                                iconRes = OpenChatIcons.GroupPerson,
                                label = "Chat with friends",
                                selected = currentDestination == OpenChatDestination.CALPICO_SOCIAL,
                                selectedColor = selectedRowColor,
                                onClick = {
                                    onSelectDestination(OpenChatDestination.CALPICO_SOCIAL)
                                    onCloseDrawer()
                                },
                            )
                        }
                    }
                    if (snapshot.isFlagEnabled("feature_finances_dashboard")) {
                        item {
                            DrawerMenuRow(
                                iconRes = OpenChatIcons.CircleDollar,
                                label = "Finances & Wallet",
                                selected = currentDestination == OpenChatDestination.FINANCE_AND_VERTICALS,
                                selectedColor = selectedRowColor,
                                onClick = {
                                    onSelectDestination(OpenChatDestination.FINANCE_AND_VERTICALS)
                                    onCloseDrawer()
                                },
                            )
                        }
                    }
                    item {
                        DrawerMenuRow(
                            iconRes = OpenChatIcons.Lock,
                            label = "Locked chats & safety",
                            selected = currentDestination == OpenChatDestination.LOCKED_CHATS_AND_SAFETY,
                            selectedColor = selectedRowColor,
                            onClick = {
                                onSelectDestination(OpenChatDestination.LOCKED_CHATS_AND_SAFETY)
                                onCloseDrawer()
                            },
                        )
                    }
                    item {
                        DrawerMenuRow(
                            iconRes = OpenChatIcons.CreditCard,
                            label = "Subscription & plans",
                            selected = currentDestination == OpenChatDestination.AUTH_AND_SUBSCRIPTION,
                            selectedColor = selectedRowColor,
                            onClick = {
                                onSelectDestination(OpenChatDestination.AUTH_AND_SUBSCRIPTION)
                                onCloseDrawer()
                            },
                        )
                    }
                    item {
                        DrawerMenuRow(
                            iconRes = OpenChatIcons.Flask,
                            label = "Experiment Override",
                            selected = currentDestination == OpenChatDestination.EXPERIMENT_INSPECTOR,
                            selectedColor = selectedRowColor,
                            onClick = {
                                onSelectDestination(OpenChatDestination.EXPERIMENT_INSPECTOR)
                                onCloseDrawer()
                            },
                        )
                    }
                }
            }

            // Bottom Profile Footer (authentic ChatGPT Android bottom bar)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        onSelectDestination(OpenChatDestination.SETTINGS_AND_PROFILE)
                        onCloseDrawer()
                    },
                color = Color.Transparent,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(OpenChatPalette.BrandPurple),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = snapshot.userFullName.take(2).uppercase(),
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                        Text(
                            text = snapshot.userFullName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        if (snapshot.subscriptionTier != SubscriptionTier.FREE) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                            ) {
                                Text(
                                    text = snapshot.subscriptionTier.badge,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                )
                            }
                        }
                    }
                    DsIcon(
                        drawableRes = OpenChatIcons.EllipsisHorizontal,
                        contentDescription = "Settings",
                        size = 18.dp,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerMenuRow(
    iconRes: Int,
    label: String,
    selected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        color = if (selected) selectedColor else Color.Transparent,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            DsIcon(
                drawableRes = iconRes,
                contentDescription = label,
                size = 20.dp,
                tint = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun DrawerSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 14.dp, bottom = 4.dp),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DrawerThreadRow(
    thread: ConversationThread,
    isSelected: Boolean,
    selectedColor: Color,
    menuExpanded: Boolean,
    onOpenMenu: () -> Unit,
    onDismissMenu: () -> Unit,
    onSelect: () -> Unit,
    onTogglePin: () -> Unit,
    onToggleArchive: () -> Unit,
    onToggleLock: () -> Unit,
    onDelete: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .combinedClickable(
                    onClick = onSelect,
                    onLongClick = onOpenMenu,
                ),
            color = if (isSelected) selectedColor else Color.Transparent,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = thread.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    if (thread.isLocked) {
                        DsIcon(
                            drawableRes = OpenChatIcons.Lock,
                            contentDescription = "Locked",
                            size = 14.dp,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    } else if (thread.isPinned) {
                        DsIcon(
                            drawableRes = OpenChatIcons.Pin,
                            contentDescription = "Pinned",
                            size = 14.dp,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    if (isSelected) {
                        IconButton(
                            onClick = onOpenMenu,
                            modifier = Modifier.size(22.dp),
                        ) {
                            DsIcon(
                                drawableRes = OpenChatIcons.EllipsisHorizontal,
                                contentDescription = "Conversation options",
                                size = 16.dp,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = onDismissMenu,
        ) {
            DropdownMenuItem(
                text = { Text(if (thread.isPinned) "Unpin chat" else "Pin chat") },
                leadingIcon = {
                    DsIcon(drawableRes = OpenChatIcons.Pin, contentDescription = null, size = 18.dp)
                },
                onClick = {
                    onTogglePin()
                    onDismissMenu()
                },
            )
            DropdownMenuItem(
                text = { Text("Archive") },
                leadingIcon = {
                    DsIcon(drawableRes = OpenChatIcons.Archive, contentDescription = null, size = 18.dp)
                },
                onClick = {
                    onToggleArchive()
                    onDismissMenu()
                },
            )
            DropdownMenuItem(
                text = { Text(if (thread.isLocked) "Unlock chat" else "Lock chat") },
                leadingIcon = {
                    DsIcon(drawableRes = OpenChatIcons.Lock, contentDescription = null, size = 18.dp)
                },
                onClick = {
                    onToggleLock()
                    onDismissMenu()
                },
            )
            DropdownMenuItem(
                text = { Text("Delete", color = OpenChatPalette.CallDeclineRed) },
                leadingIcon = {
                    DsIcon(
                        drawableRes = OpenChatIcons.Trash,
                        contentDescription = null,
                        size = 18.dp,
                        tint = OpenChatPalette.CallDeclineRed,
                    )
                },
                onClick = {
                    onDelete()
                    onDismissMenu()
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ConversationScreen(
    snapshot: AppSnapshot,
    controller: FakeStateController,
    onNavigate: (OpenChatDestination) -> Unit,
    onOpenDrawer: () -> Unit = {},
    onOpenDebugSheet: () -> Unit = {},
) {
    var promptInput by remember { mutableStateOf("") }
    var showModelSheet by remember { mutableStateOf(false) }
    var showAttachmentSheet by remember { mutableStateOf(false) }
    var showToolsSheet by remember { mutableStateOf(false) }
    var showOverflowMenu by remember { mutableStateOf(false) }

    val activeThread = snapshot.activeConversation
    val isDark = MaterialTheme.colorScheme.background == OpenChatPalette.DarkBgPrimary
    val currentModel = snapshot.models.find { it.slug == snapshot.selectedModelSlug }
    val modelShortLabel = when (snapshot.selectedModelSlug) {
        "gpt-5.4" -> "5.4"
        "gpt-5.5-pro" -> "5.5 Pro"
        "gpt-4o" -> "4o"
        "gpt-4o-mini" -> "4o mini"
        "dynamic-router" -> "Auto"
        else -> "Canary"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // Authentic ChatGPT Android Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // Left: Sidebar Menu button (2 asymmetric horizontal lines)
            IconButton(onClick = onOpenDrawer) {
                DsIcon(
                    drawableRes = OpenChatIcons.SidebarMenu,
                    contentDescription = "Open navigation drawer",
                    size = 24.dp,
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }

            // Center: "ChatGPT 5.4 v" Model Picker or "Get Plus" Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .combinedClickable(
                            onClick = { showModelSheet = true },
                            onLongClick = onOpenDebugSheet,
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "ChatGPT",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = modelShortLabel,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    DsIcon(
                        drawableRes = OpenChatIcons.ChevronDown,
                        contentDescription = "Select model",
                        size = 16.dp,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                if (snapshot.subscriptionTier == SubscriptionTier.FREE) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isDark) OpenChatPalette.PlusPillBgDark else OpenChatPalette.PlusPillBgLight,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onNavigate(OpenChatDestination.AUTH_AND_SUBSCRIPTION) },
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            DsIcon(
                                drawableRes = OpenChatIcons.Sparkles,
                                contentDescription = null,
                                size = 14.dp,
                                tint = if (isDark) OpenChatPalette.PlusPillTextDark else OpenChatPalette.PlusPillTextLight,
                            )
                            Text(
                                text = "Get Plus",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDark) OpenChatPalette.PlusPillTextDark else OpenChatPalette.PlusPillTextLight,
                            )
                        }
                    }
                }
            }

            // Right: Temporary Chat / New Chat + Overflow Menu
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (activeThread == null || activeThread.messages.isEmpty()) {
                    IconButton(onClick = { controller.toggleTemporaryChat() }) {
                        DsIcon(
                            drawableRes = OpenChatIcons.ChatBubbleDashed,
                            contentDescription = "Temporary chat",
                            size = 22.dp,
                            tint = if (snapshot.isTemporaryChat) {
                                OpenChatPalette.AccentBlue
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            },
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            controller.startNewChat(mode = ConversationMode.DEFAULT, isTemporary = false)
                        },
                    ) {
                        DsIcon(
                            drawableRes = OpenChatIcons.ComposeNewChat,
                            contentDescription = "New chat",
                            size = 22.dp,
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }

                Box {
                    IconButton(onClick = { showOverflowMenu = true }) {
                        DsIcon(
                            drawableRes = OpenChatIcons.EllipsisVertical,
                            contentDescription = "More options",
                            size = 22.dp,
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }

                    DropdownMenu(
                        expanded = showOverflowMenu,
                        onDismissRequest = { showOverflowMenu = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text("Share") },
                            leadingIcon = {
                                DsIcon(drawableRes = OpenChatIcons.ShareAndroid, contentDescription = null, size = 18.dp)
                            },
                            onClick = {
                                showOverflowMenu = false
                                controller.showToastBanner("Share link copied to clipboard")
                            },
                        )
                        if (activeThread != null) {
                            DropdownMenuItem(
                                text = { Text(if (activeThread.isPinned) "Unpin chat" else "Pin chat") },
                                leadingIcon = {
                                    DsIcon(drawableRes = OpenChatIcons.Pin, contentDescription = null, size = 18.dp)
                                },
                                onClick = {
                                    showOverflowMenu = false
                                    controller.togglePinConversation(activeThread.id)
                                },
                            )
                            DropdownMenuItem(
                                text = { Text("Archive") },
                                leadingIcon = {
                                    DsIcon(drawableRes = OpenChatIcons.Archive, contentDescription = null, size = 18.dp)
                                },
                                onClick = {
                                    showOverflowMenu = false
                                    controller.toggleArchiveConversation(activeThread.id)
                                },
                            )
                            DropdownMenuItem(
                                text = { Text(if (activeThread.isLocked) "Unlock chat" else "Move to Locked chats") },
                                leadingIcon = {
                                    DsIcon(drawableRes = OpenChatIcons.Lock, contentDescription = null, size = 18.dp)
                                },
                                onClick = {
                                    showOverflowMenu = false
                                    controller.toggleLockConversation(activeThread.id)
                                },
                            )
                        }
                        DropdownMenuItem(
                            text = { Text(if (snapshot.isTemporaryChat) "Turn off Temporary chat" else "Temporary chat") },
                            leadingIcon = {
                                DsIcon(drawableRes = OpenChatIcons.ChatBubbleDashed, contentDescription = null, size = 18.dp)
                            },
                            onClick = {
                                showOverflowMenu = false
                                controller.toggleTemporaryChat()
                            },
                        )
                        DropdownMenuItem(
                            text = { Text("Switch model (${currentModel?.name ?: snapshot.selectedModelSlug})") },
                            leadingIcon = {
                                DsIcon(drawableRes = OpenChatIcons.Sparkles, contentDescription = null, size = 18.dp)
                            },
                            onClick = {
                                showOverflowMenu = false
                                showModelSheet = true
                            },
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Developer State & Flags") },
                            leadingIcon = {
                                DsIcon(drawableRes = OpenChatIcons.Flask, contentDescription = null, size = 18.dp)
                            },
                            onClick = {
                                showOverflowMenu = false
                                onOpenDebugSheet()
                            },
                        )
                    }
                }
            }
        }

        // Main Chat Stream / Empty State
        StateSimulationHost(
            state = snapshot.uiSimState,
            loadingTitle = "Loading conversation...",
            emptyTitle = "What can I help with?",
            emptySubtitle = "Ask anything, search the web, analyze files, or start a voice conversation.",
            emptyActionLabel = "Ask a starter question",
            onEmptyAction = {
                controller.setUiSimState(UiSimState.SUCCESS)
                controller.sendUserMessage("Explain spatial spring motion tokens in Material 3")
            },
            errorTitle = "Something went wrong",
            errorSubtitle = "We couldn't load this conversation. Check your connection and try again.",
            onRetry = { controller.setUiSimState(UiSimState.SUCCESS) },
            modifier = Modifier.weight(1f),
        ) {
            if (activeThread == null || activeThread.messages.isEmpty()) {
                EmptyConversationCenter(
                    isTemporaryChat = snapshot.isTemporaryChat,
                    onSelectPrompt = { prompt -> controller.sendUserMessage(prompt) },
                    onNavigate = onNavigate,
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    item { Spacer(modifier = Modifier.height(4.dp)) }

                    if (snapshot.isTemporaryChat) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    DsIcon(
                                        drawableRes = OpenChatIcons.ChatBubbleDashed,
                                        contentDescription = null,
                                        size = 18.dp,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                    Text(
                                        text = "Temporary chat won't appear in history or update memory.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }
                    }

                    if (activeThread.branchedFromTitle != null) {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                DsIcon(
                                    drawableRes = OpenChatIcons.ArrowCurvedRight,
                                    contentDescription = null,
                                    size = 15.dp,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Text(
                                    text = "Branched from ${activeThread.branchedFromTitle}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }

                    items(activeThread.messages, key = { it.id }) { message ->
                        AuthenticMessageItem(
                            message = message,
                            variantsFlagEnabled = snapshot.isFlagEnabled("feature_variants_in_stream"),
                            onSelectVariant = { idx -> controller.selectMessageVariant(message.id, idx) },
                            onActionFeedback = { msg -> controller.showToastBanner(msg) },
                        )
                    }

                    if (snapshot.subscriptionTier == SubscriptionTier.FREE &&
                        snapshot.isFlagEnabled("feature_ads_transparency_controls")
                    ) {
                        item {
                            SponsoredAdCard(
                                onUpgradeClick = { onNavigate(OpenChatDestination.AUTH_AND_SUBSCRIPTION) },
                                onManageAdsClick = { onNavigate(OpenChatDestination.SETTINGS_AND_PROFILE) },
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(12.dp)) }
                }
            }
        }

        // Authentic 2-Tier Pill Composer Input Bar
        ComposerInputBar(
            promptInput = promptInput,
            onPromptChange = { promptInput = it },
            isWebSearchEnabled = snapshot.isWebSearchEnabled,
            isDeepResearchEnabled = snapshot.isDeepResearchEnabled,
            isWorkMode = snapshot.isWorkMode,
            onToggleWebSearch = { controller.toggleWebSearch() },
            onToggleDeepResearch = { controller.toggleDeepResearch() },
            onOpenAttachmentSheet = { showAttachmentSheet = true },
            onOpenToolsSheet = { showToolsSheet = true },
            onSend = {
                controller.sendUserMessage(promptInput)
                promptInput = ""
            },
            onOpenVoiceCall = { onNavigate(OpenChatDestination.VOICE_CALL) },
        )
    }

    // Model & Reasoning Effort Picker Bottom Sheet
    if (showModelSheet) {
        ModalBottomSheet(
            onDismissRequest = { showModelSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = "Model",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )

                snapshot.models.forEach { model ->
                    val isSelected = model.slug == snapshot.selectedModelSlug
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                controller.selectModel(model.slug)
                                showModelSheet = false
                            },
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.surfaceVariant
                        } else {
                            Color.Transparent
                        },
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = model.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                )
                                Text(
                                    text = model.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            if (isSelected) {
                                DsIcon(
                                    drawableRes = OpenChatIcons.Checkmark,
                                    contentDescription = "Selected",
                                    size = 20.dp,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }
                    }
                }

                if (snapshot.isFlagEnabled("feature_reasoning_effort_gauge")) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        text = "Reasoning effort",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        ReasoningEffort.entries.forEach { effort ->
                            FilterChip(
                                selected = snapshot.selectedEffort == effort,
                                onClick = { controller.selectEffort(effort) },
                                label = { Text(effort.label) },
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // "+" Attachment Bottom Sheet (Camera, Photos, Files, Sketch)
    if (showAttachmentSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAttachmentSheet = false },
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    AttachmentTileButton(
                        iconRes = OpenChatIcons.Camera,
                        label = "Camera",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            showAttachmentSheet = false
                            onNavigate(OpenChatDestination.CAMERA_SKETCH)
                        },
                    )
                    AttachmentTileButton(
                        iconRes = OpenChatIcons.ImageSquare,
                        label = "Photos",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            showAttachmentSheet = false
                            controller.sendUserMessage(
                                prompt = "Analyze this photo and extract key details",
                                attachmentTitle = "IMG_2026_Photo.jpg",
                            )
                        },
                    )
                    AttachmentTileButton(
                        iconRes = OpenChatIcons.Folder,
                        label = "Files",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            showAttachmentSheet = false
                            onNavigate(OpenChatDestination.ARTIFACT_LIBRARY)
                        },
                    )
                }

                HorizontalDivider()

                DrawerMenuRow(
                    iconRes = OpenChatIcons.PaintbrushOnPencil,
                    label = "Sketch & annotate image",
                    selected = false,
                    selectedColor = Color.Transparent,
                    onClick = {
                        showAttachmentSheet = false
                        onNavigate(OpenChatDestination.CAMERA_SKETCH)
                    },
                )
                DrawerMenuRow(
                    iconRes = OpenChatIcons.Globe,
                    label = "Connect Google Drive or GitHub",
                    selected = false,
                    selectedColor = Color.Transparent,
                    onClick = {
                        showAttachmentSheet = false
                        onNavigate(OpenChatDestination.DISCOVERY_HUB)
                    },
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }

    // Sliders "Tools" Bottom Sheet (Search the web, Deep research, Create an image, Work mode)
    if (showToolsSheet) {
        ModalBottomSheet(
            onDismissRequest = { showToolsSheet = false },
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "Tools",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                )
                ToolSheetRow(
                    iconRes = OpenChatIcons.PhotoSparkle,
                    title = "Create an image",
                    subtitle = "Visualize ideas and edit images",
                    active = false,
                    onClick = {
                        showToolsSheet = false
                        onNavigate(OpenChatDestination.CAMERA_SKETCH)
                    },
                )
                ToolSheetRow(
                    iconRes = OpenChatIcons.Globe,
                    title = "Search the web",
                    subtitle = "Find real-time news and verified citations",
                    active = snapshot.isWebSearchEnabled,
                    onClick = {
                        controller.toggleWebSearch()
                        showToolsSheet = false
                    },
                )
                ToolSheetRow(
                    iconRes = OpenChatIcons.Telescope,
                    title = "Deep research",
                    subtitle = "In-depth multi-step synthesis with sources",
                    active = snapshot.isDeepResearchEnabled,
                    onClick = {
                        controller.toggleDeepResearch()
                        showToolsSheet = false
                    },
                )
                ToolSheetRow(
                    iconRes = OpenChatIcons.Lightbulb,
                    title = "Think longer",
                    subtitle = "Current effort: ${snapshot.selectedEffort.label}",
                    active = snapshot.selectedEffort != ReasoningEffort.LOW,
                    onClick = {
                        val nextIndex = (ReasoningEffort.entries.indexOf(snapshot.selectedEffort) + 1) %
                            ReasoningEffort.entries.size
                        controller.selectEffort(ReasoningEffort.entries[nextIndex])
                        showToolsSheet = false
                    },
                )
                ToolSheetRow(
                    iconRes = OpenChatIcons.Suitcase,
                    title = "Work mode",
                    subtitle = "Reference connected workspace docs and apps",
                    active = snapshot.isWorkMode,
                    onClick = {
                        controller.toggleWorkMode()
                        showToolsSheet = false
                    },
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun AttachmentTileButton(
    iconRes: Int,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier
            .height(84.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            DsIcon(
                drawableRes = iconRes,
                contentDescription = label,
                size = 24.dp,
                tint = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun ToolSheetRow(
    iconRes: Int,
    title: String,
    subtitle: String,
    active: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        color = if (active) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f),
            ) {
                DsIcon(
                    drawableRes = iconRes,
                    contentDescription = title,
                    size = 22.dp,
                    tint = if (active) OpenChatPalette.AccentBlue else MaterialTheme.colorScheme.onSurface,
                )
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = if (active) OpenChatPalette.AccentBlue else MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            if (active) {
                DsIcon(
                    drawableRes = OpenChatIcons.Checkmark,
                    contentDescription = "Enabled",
                    size = 18.dp,
                    tint = OpenChatPalette.AccentBlue,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EmptyConversationCenter(
    isTemporaryChat: Boolean,
    onSelectPrompt: (String) -> Unit,
    onNavigate: (OpenChatDestination) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(22.dp),
        ) {
            if (isTemporaryChat) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        DsIcon(
                            drawableRes = OpenChatIcons.ChatBubbleDashed,
                            contentDescription = null,
                            size = 16.dp,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "Temporary Chat",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            Text(
                text = "What can I help with?",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                SuggestionPillChip(
                    iconRes = OpenChatIcons.PhotoSparkle,
                    iconTint = Color(0xFF10A37F),
                    label = "Create image",
                    onClick = { onNavigate(OpenChatDestination.CAMERA_SKETCH) },
                )
                SuggestionPillChip(
                    iconRes = OpenChatIcons.Notebook,
                    iconTint = Color(0xFFEA580C),
                    label = "Summarize text",
                    onClick = {
                        onSelectPrompt("Summarize the key architectural decisions in our Android project")
                    },
                )
                SuggestionPillChip(
                    iconRes = OpenChatIcons.Braces,
                    iconTint = Color(0xFF8B5CF6),
                    label = "Code",
                    onClick = {
                        onSelectPrompt("Write a clean Kotlin StateFlow holder with unit test coverage")
                    },
                )
                SuggestionPillChip(
                    iconRes = OpenChatIcons.BarChart,
                    iconTint = Color(0xFF0285FF),
                    label = "Analyze data",
                    onClick = {
                        onSelectPrompt("Analyze p50 and p99 latency trends across streaming endpoints")
                    },
                )
                SuggestionPillChip(
                    iconRes = OpenChatIcons.Lightbulb,
                    iconTint = Color(0xFFEAB308),
                    label = "Brainstorm",
                    onClick = {
                        onSelectPrompt("Brainstorm 5 offline-first mobile UX improvements")
                    },
                )
            }
        }
    }
}

@Composable
private fun SuggestionPillChip(
    iconRes: Int,
    iconTint: Color,
    label: String,
    onClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(20.dp),
            )
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DsIcon(
                drawableRes = iconRes,
                contentDescription = null,
                size = 17.dp,
                tint = iconTint,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun AuthenticMessageItem(
    message: ChatMessage,
    variantsFlagEnabled: Boolean,
    onSelectVariant: (Int) -> Unit,
    onActionFeedback: (String) -> Unit,
) {
    var showReasoning by remember { mutableStateOf(false) }
    var showSources by remember { mutableStateOf(false) }
    val isDark = MaterialTheme.colorScheme.background == OpenChatPalette.DarkBgPrimary

    if (message.isUser) {
        // Authentic Right-Aligned User Pill Bubble (No "You" label, no box border)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (message.imageAttachmentTitle != null) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        DsIcon(
                            drawableRes = OpenChatIcons.ImageSquare,
                            contentDescription = null,
                            size = 16.dp,
                        )
                        Text(
                            text = message.imageAttachmentTitle,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            }

            Surface(
                shape = RoundedCornerShape(22.dp),
                color = if (isDark) OpenChatPalette.DarkBgUserMessage else OpenChatPalette.LightBgUserMessage,
                modifier = Modifier.fillMaxWidth(0.82f),
            ) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        lineHeight = 23.sp,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 11.dp),
                )
            }
        }
    } else {
        // Authentic Unboxed Full-Width Assistant Message directly on Background Canvas
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Inline "Thought for Xs >" Toggle
            if (message.thinkingSeconds > 0 && message.reasoningSteps.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showReasoning = !showReasoning }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "Thought for ${message.thinkingSeconds}s",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        DsIcon(
                            drawableRes = if (showReasoning) {
                                OpenChatIcons.ChevronDown
                            } else {
                                OpenChatIcons.ChevronRight
                            },
                            contentDescription = "Toggle reasoning steps",
                            size = 15.dp,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    AnimatedVisibility(visible = showReasoning) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height((message.reasoningSteps.size * 28).dp)
                                    .background(MaterialTheme.colorScheme.outline),
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                message.reasoningSteps.forEach { step ->
                                    Text(
                                        text = step,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Subtle Inline Tool Execution Summary
            if (message.toolSteps.isNotEmpty()) {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    message.toolSteps.forEach { tool ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                DsIcon(
                                    drawableRes = OpenChatIcons.Checkmark,
                                    contentDescription = null,
                                    size = 13.dp,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Text(
                                    text = tool.toolName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
            }

            // Formatted Assistant Text
            AssistantFormattedText(text = message.text)

            // Authentic Code Block Card
            message.codeSnippets.forEach { snippet ->
                val codeBg = if (isDark) OpenChatPalette.DarkBgCodeBlock else OpenChatPalette.LightBgCodeBlock
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = codeBg,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(12.dp),
                        ),
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = snippet.language,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { onActionFeedback("Copied code to clipboard") }
                                    .padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                DsIcon(
                                    drawableRes = OpenChatIcons.Copy,
                                    contentDescription = "Copy code",
                                    size = 14.dp,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Text(
                                    text = "Copy code",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                        Text(
                            text = snippet.code,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 19.sp,
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(14.dp),
                        )
                        if (snippet.output != null) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                            Text(
                                text = snippet.output,
                                style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                                color = OpenChatPalette.CallAnswerGreen,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            )
                        }
                    }
                }
            }

            // Compact "Sources" Pill + Expandable Citations
            if (message.citations.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { showSources = !showSources },
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            DsIcon(
                                drawableRes = OpenChatIcons.Globe,
                                contentDescription = null,
                                size = 14.dp,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = "Sources (${message.citations.size})",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }

                    AnimatedVisibility(visible = showSources) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            message.citations.forEach { citation ->
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = citation.title,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.SemiBold,
                                        )
                                        Text(
                                            text = citation.domain,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = OpenChatPalette.AccentBlue,
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
                }
            }

            // Variants In Stream Switcher
            if (variantsFlagEnabled && message.variants.size > 1) {
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

            // Authentic ChatGPT Action Row (Copy, Read Aloud, Thumbs Up, Thumbs Down, Regenerate, Share)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                MessageActionIconButton(
                    iconRes = OpenChatIcons.Copy,
                    contentDescription = "Copy response",
                    onClick = { onActionFeedback("Copied to clipboard") },
                )
                MessageActionIconButton(
                    iconRes = OpenChatIcons.SpeakerWave,
                    contentDescription = "Read aloud",
                    onClick = { onActionFeedback("Reading response aloud") },
                )
                MessageActionIconButton(
                    iconRes = OpenChatIcons.ThumbUp,
                    contentDescription = "Good response",
                    onClick = { onActionFeedback("Thanks for your feedback") },
                )
                MessageActionIconButton(
                    iconRes = OpenChatIcons.ThumbDown,
                    contentDescription = "Bad response",
                    onClick = { onActionFeedback("Feedback recorded") },
                )
                MessageActionIconButton(
                    iconRes = OpenChatIcons.ArrowRotateClockwise,
                    contentDescription = "Regenerate response",
                    onClick = {
                        if (message.variants.size > 1) {
                            val nextIdx = (message.selectedVariantIndex + 1) % message.variants.size
                            onSelectVariant(nextIdx)
                        } else {
                            onActionFeedback("Regenerated response")
                        }
                    },
                )
                MessageActionIconButton(
                    iconRes = OpenChatIcons.ShareAndroid,
                    contentDescription = "Share response",
                    onClick = { onActionFeedback("Share sheet opened") },
                )
            }
        }
    }
}

@Composable
private fun AssistantFormattedText(text: String) {
    val cleanText = text.replace("**", "")
    val paragraphs = cleanText.split("\n\n")
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        paragraphs.forEach { block ->
            val trimmed = block.trim()
            if (trimmed.startsWith("### ")) {
                Text(
                    text = trimmed.removePrefix("### "),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            } else {
                Text(
                    text = trimmed,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

@Composable
private fun MessageActionIconButton(
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(34.dp),
    ) {
        DsIcon(
            drawableRes = iconRes,
            contentDescription = contentDescription,
            size = 18.dp,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SponsoredAdCard(
    onUpgradeClick: () -> Unit,
    onManageAdsClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StatusBadge(text = "Sponsored")
                Text(
                    text = "Go ad-free with Plus",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = "Upgrade to ChatGPT Plus for higher limits and zero ads.",
                style = MaterialTheme.typography.bodySmall,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onUpgradeClick) {
                    Text("Get Plus", style = MaterialTheme.typography.labelSmall)
                }
                OutlinedButton(onClick = onManageAdsClick) {
                    Text("Ad settings", style = MaterialTheme.typography.labelSmall)
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
    isWorkMode: Boolean,
    onToggleWebSearch: () -> Unit,
    onToggleDeepResearch: () -> Unit,
    onOpenAttachmentSheet: () -> Unit,
    onOpenToolsSheet: () -> Unit,
    onSend: () -> Unit,
    onOpenVoiceCall: () -> Unit,
) {
    val isDark = MaterialTheme.colorScheme.background == OpenChatPalette.DarkBgPrimary
    val composerBg = if (isDark) OpenChatPalette.DarkBgTertiary else OpenChatPalette.LightBgTertiary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            color = composerBg,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // Top Tier: Borderless Text Field ("Ask anything")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp, vertical = 2.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (promptInput.isEmpty()) {
                        Text(
                            text = "Ask anything",
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    BasicTextField(
                        value = promptInput,
                        onValueChange = onPromptChange,
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                        maxLines = 6,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                // Bottom Tier inside Pill: [+] [Sliders / Active Tool Pills] ... [Mic] [Voice / Send Circle]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    // Left controls: "+" Attachment & Sliders Tools + Active Tool Chips
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState()),
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .clickable(onClick = onOpenAttachmentSheet),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.background.copy(alpha = 0.65f),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                DsIcon(
                                    drawableRes = OpenChatIcons.Plus,
                                    contentDescription = "Add photos or files",
                                    size = 20.dp,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .height(34.dp)
                                .clip(RoundedCornerShape(17.dp))
                                .clickable(onClick = onOpenToolsSheet),
                            shape = RoundedCornerShape(17.dp),
                            color = MaterialTheme.colorScheme.background.copy(alpha = 0.65f),
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                DsIcon(
                                    drawableRes = OpenChatIcons.Sliders,
                                    contentDescription = "Tools",
                                    size = 17.dp,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                                if (!isWebSearchEnabled && !isDeepResearchEnabled) {
                                    Text(
                                        text = "Tools",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                }
                            }
                        }

                        if (isWebSearchEnabled) {
                            ActiveComposerToolPill(
                                iconRes = OpenChatIcons.Globe,
                                label = "Search",
                                onDismiss = onToggleWebSearch,
                            )
                        }

                        if (isDeepResearchEnabled) {
                            ActiveComposerToolPill(
                                iconRes = OpenChatIcons.Telescope,
                                label = "Deep research",
                                onDismiss = onToggleDeepResearch,
                            )
                        }

                        if (isWorkMode) {
                            Surface(
                                shape = RoundedCornerShape(17.dp),
                                color = OpenChatPalette.AccentBlue.copy(alpha = 0.14f),
                            ) {
                                Text(
                                    text = "Work",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = OpenChatPalette.AccentBlue,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                )
                            }
                        }
                    }

                    // Right controls: Mic Dictation + Filled Black/White Voice/Send Button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        IconButton(
                            onClick = {
                                if (promptInput.isBlank()) {
                                    onPromptChange("Summarize the key points from today's architecture review")
                                } else {
                                    onSend()
                                }
                            },
                            modifier = Modifier.size(36.dp),
                        ) {
                            DsIcon(
                                drawableRes = OpenChatIcons.Microphone,
                                contentDescription = "Dictate",
                                size = 21.dp,
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }

                        Surface(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .clickable {
                                    if (promptInput.isBlank()) {
                                        onOpenVoiceCall()
                                    } else {
                                        onSend()
                                    }
                                },
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                DsIcon(
                                    drawableRes = if (promptInput.isBlank()) {
                                        OpenChatIcons.VoiceButton
                                    } else {
                                        OpenChatIcons.ArrowUpButton
                                    },
                                    contentDescription = if (promptInput.isBlank()) {
                                        "Start voice mode"
                                    } else {
                                        "Send message"
                                    },
                                    size = 20.dp,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActiveComposerToolPill(
    iconRes: Int,
    label: String,
    onDismiss: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .height(34.dp)
            .clip(RoundedCornerShape(17.dp))
            .clickable(onClick = onDismiss),
        shape = RoundedCornerShape(17.dp),
        color = OpenChatPalette.AccentBlue.copy(alpha = 0.14f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            DsIcon(
                drawableRes = iconRes,
                contentDescription = null,
                size = 15.dp,
                tint = OpenChatPalette.AccentBlue,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = OpenChatPalette.AccentBlue,
            )
            DsIcon(
                drawableRes = OpenChatIcons.CloseX,
                contentDescription = "Remove $label",
                size = 12.dp,
                tint = OpenChatPalette.AccentBlue,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceCallScreen(
    snapshot: AppSnapshot,
    controller: FakeStateController,
    onEndCall: () -> Unit = {},
) {
    var showVoicePickerSheet by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "VoiceOrbPulse")
    val orbScale by infiniteTransition.animateFloat(
        initialValue = 0.93f,
        targetValue = 1.07f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "OrbScaleAnimation",
    )

    val currentVoice = snapshot.voicePresets.find { it.id == snapshot.selectedVoiceId }
        ?: snapshot.voicePresets.first()

    StateSimulationHost(
        state = snapshot.uiSimState,
        loadingTitle = "Connecting to Voice...",
        emptyTitle = "Voice is unavailable",
        emptySubtitle = "Select a voice preset or enable microphone permissions to start talking.",
        errorTitle = "Voice connection interrupted",
        errorSubtitle = "Please check your connection and try again.",
        onRetry = { controller.setUiSimState(UiSimState.SUCCESS) },
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            // Top Floating Controls Row (Voice pill on left, Captions + Voice Settings Sliders on right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { showVoicePickerSheet = true },
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = currentVoice.name,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium,
                        )
                        DsIcon(
                            drawableRes = OpenChatIcons.ChevronDown,
                            contentDescription = "Choose voice",
                            size = 14.dp,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = { controller.toggleVoiceCaptions() }) {
                        DsIcon(
                            drawableRes = OpenChatIcons.ChatBubble,
                            contentDescription = "Toggle captions",
                            size = 22.dp,
                            tint = if (snapshot.isVoiceCaptionsEnabled) {
                                OpenChatPalette.AccentBlue
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                        )
                    }
                    IconButton(onClick = { showVoicePickerSheet = true }) {
                        DsIcon(
                            drawableRes = OpenChatIcons.Sliders,
                            contentDescription = "Voice settings",
                            size = 22.dp,
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }

            // Center Fluid Blue Voice Orb + Optional Camera Viewfinder + Captions
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .scale(if (snapshot.isVoiceMuted) 1f else orbScale)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = if (snapshot.isVoiceMuted) {
                                    listOf(
                                        OpenChatPalette.LightTextTertiary,
                                        OpenChatPalette.DarkBgTertiary,
                                    )
                                } else {
                                    listOf(
                                        OpenChatPalette.VoiceOrbLightBlue,
                                        OpenChatPalette.VoiceOrbMidBlue,
                                        OpenChatPalette.VoiceOrbDeepBlue,
                                    )
                                },
                            ),
                        ),
                )

                Text(
                    text = when {
                        snapshot.isVoiceMuted -> "Muted"
                        snapshot.isVoiceCameraLive -> "Listening with Live Camera"
                        else -> "Listening"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                )

                if (snapshot.isVoiceCaptionsEnabled) {
                    Text(
                        text = "\"Hi! I'm ${currentVoice.name}. Ask me anything or share your camera whenever you're ready.\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }

                AnimatedVisibility(visible = snapshot.showVoiceDebugOverlay) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = OpenChatPalette.DarkBgDrawer,
                        contentColor = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(
                                text = "Voice Debug Telemetry (OrbitVoiceCallActivity)",
                                style = MaterialTheme.typography.labelLarge,
                                color = OpenChatPalette.VoiceOrbLightBlue,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text("Session: vsess_94a8bc01e2 | WebRTC Opus 48kHz", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
                            Text("Model: ${snapshot.selectedModelSlug}-realtime | RTT: 24 ms", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
                            Text("Concealment: 99.9% | Wingman Ducking: ON", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }

            // Bottom 3 Circular Action Buttons: [Video/Camera] [Mute/Unmute] [End Call Red X]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 1. Live Video / Camera Button
                Surface(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .clickable { controller.toggleVoiceCamera() },
                    shape = CircleShape,
                    color = if (snapshot.isVoiceCameraLive) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        DsIcon(
                            drawableRes = OpenChatIcons.Video,
                            contentDescription = "Toggle live camera",
                            size = 26.dp,
                            tint = if (snapshot.isVoiceCameraLive) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                        )
                    }
                }

                // 2. Mute / Unmute Microphone Button
                Surface(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .clickable { controller.toggleVoiceMute() },
                    shape = CircleShape,
                    color = if (snapshot.isVoiceMuted) {
                        OpenChatPalette.CallDeclineRed
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        DsIcon(
                            drawableRes = if (snapshot.isVoiceMuted) {
                                OpenChatIcons.MicrophoneSlashFill
                            } else {
                                OpenChatIcons.Microphone
                            },
                            contentDescription = if (snapshot.isVoiceMuted) "Unmute" else "Mute",
                            size = 26.dp,
                            tint = if (snapshot.isVoiceMuted) {
                                Color.White
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                        )
                    }
                }

                // 3. End Call Button (Red Circle with X)
                Surface(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onEndCall),
                    shape = CircleShape,
                    color = OpenChatPalette.CallDeclineRed,
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        DsIcon(
                            drawableRes = OpenChatIcons.CloseX,
                            contentDescription = "End voice session",
                            size = 26.dp,
                            tint = Color.White,
                        )
                    }
                }
            }
        }
    }

    if (showVoicePickerSheet) {
        ModalBottomSheet(
            onDismissRequest = { showVoicePickerSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = "Choose a voice",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )

                snapshot.voicePresets.forEach { preset ->
                    val isSelected = preset.id == snapshot.selectedVoiceId
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                controller.selectVoicePreset(preset.id)
                                showVoicePickerSheet = false
                            },
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.surfaceVariant
                        } else {
                            Color.Transparent
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
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Text(
                                    text = "${preset.tagline} - ${preset.toneDescription}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            if (isSelected) {
                                DsIcon(
                                    drawableRes = OpenChatIcons.Checkmark,
                                    contentDescription = "Selected",
                                    size = 20.dp,
                                )
                            }
                        }
                    }
                }

                HorizontalDivider()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Show Voice Debug Telemetry",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = "Displays WebRTC Opus bitrate, RTT, and concealment stats",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Switch(
                        checked = snapshot.showVoiceDebugOverlay,
                        onCheckedChange = { controller.toggleVoiceDebugOverlay() },
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
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
                title = "Create & Edit Images",
                subtitle = "Draw on the canvas, choose an aspect ratio, or describe an image",
                actionLabel = flashMode,
                onActionClick = {
                    flashMode = when (flashMode) {
                        "Flash auto" -> "Flash on"
                        "Flash on" -> "Flash off"
                        else -> "Flash auto"
                    }
                },
            )

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
                        drawableRes = OpenChatIcons.PhotoSparkle,
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
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Create stickers")
                }
            }
        }
    }
}
