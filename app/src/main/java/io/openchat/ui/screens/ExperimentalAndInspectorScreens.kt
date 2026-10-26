package io.openchat.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.openchat.data.mock.AppSnapshot
import io.openchat.data.mock.FakeStateController
import io.openchat.data.mock.SubscriptionTier
import io.openchat.data.mock.UiSimState
import io.openchat.navigation.OpenChatDestination
import io.openchat.ui.components.DsIcon
import io.openchat.ui.components.SectionHeader
import io.openchat.ui.components.StateSimulationHost
import io.openchat.ui.components.StatusBadge
import io.openchat.ui.theme.OpenChatDimens
import io.openchat.ui.theme.OpenChatIcons
import io.openchat.ui.theme.OpenChatMotion
import io.openchat.ui.theme.OpenChatPalette

@Composable
fun CodexRemoteScreen(
    snapshot: AppSnapshot,
    controller: FakeStateController,
) {
    var pairingCode by remember { mutableStateOf("A1B2-C3D4") }
    var remoteCommandInput by remember { mutableStateOf("/status") }
    var pairingConfirmed by remember { mutableStateOf(true) }

    StateSimulationHost(
        state = snapshot.uiSimState,
        loadingTitle = "Looking for remote host & loading environments...",
        emptyTitle = "No cloud environments or remote tasks",
        emptySubtitle = "Connect to Codex on your desktop computer, or work in the cloud by starting a chat below.",
        errorTitle = "Couldn't reach your computer or Codex Cloud",
        errorSubtitle = "Make sure this computer is awake and the Open Chat desktop app is open.",
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
                title = "Build with Codex, wherever you go",
                subtitle = "Start coding tasks in a cloud environment or pair with your desktop computer (m1v, bmp0).",
            )

            // Desktop QR / Code Pairing Card (`strings.xml#L1486-L1509`)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                modifier = Modifier.fillMaxWidth(),
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
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            DsIcon(
                                drawableRes = OpenChatIcons.LaptopConnector,
                                contentDescription = null,
                                size = 20.dp,
                            )
                            Text(
                                text = "Connect computer (Codex Remote Pairing)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                        StatusBadge(text = if (pairingConfirmed) "Connected" else "Pairing..")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        OutlinedTextField(
                            value = pairingCode,
                            onValueChange = { pairingCode = it },
                            label = { Text("Enter pairing code") },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                        )
                        Button(onClick = { pairingConfirmed = true }) {
                            Text("Pair")
                        }
                    }
                }
            }

            // Codex Companion Pets (`strings.xml#L1528-L1591`)
            if (snapshot.isFlagEnabled("feature_codex_companion_pets")) {
                SectionHeader(
                    title = "Choose your pet (Workspace Companions)",
                    subtitle = "Choose a companion that works alongside you in Work and Codex modes.",
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    snapshot.codexPets.forEach { pet ->
                        val selected = pet.id == snapshot.selectedPetId
                        Surface(
                            modifier = Modifier
                                .width(165.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { controller.selectCodexPet(pet.id) },
                            color = if (selected) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                            },
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(
                                        text = pet.asciiPreview,
                                        style = MaterialTheme.typography.titleMedium.copy(fontFamily = FontFamily.Monospace),
                                        fontWeight = FontWeight.Bold,
                                    )
                                    if (selected) {
                                        StatusBadge(text = "Active")
                                    }
                                }
                                Text(
                                    text = pet.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Text(
                                    text = pet.tagline,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                )
                            }
                        }
                    }
                }
            }

            HorizontalDivider()

            // Active Remote Next Sessions (`bmp0.java`)
            SectionHeader(
                title = "Remote Next & Cloud Sessions",
                subtitle = "Recent Codex tasks on your connected computers and Cloud.",
            )

            snapshot.codexSessions.forEach { session ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    modifier = Modifier.fillMaxWidth(),
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
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = session.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Text(
                                    text = "Host: ${session.hostName} (${session.hostType})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            StatusBadge(text = session.status)
                        }

                        Text(
                            text = "Directory: ${session.directory} | Worktree: ${session.branchWorktree}",
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                        )
                        Text(
                            text = "Context: ${session.contextUsedSummary} | Rate limit: ${session.rateLimitReset}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = "Permissions: ${session.permissionMode}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )

                        // File Changes Diff
                        if (session.fileChanges.isNotEmpty()) {
                            Text(
                                text = "File changes",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                            session.fileChanges.forEach { diff ->
                                Text(
                                    text = diff,
                                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                                )
                            }
                        }

                        // Terminal Output
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = OpenChatPalette.Neutral10,
                            contentColor = OpenChatPalette.Neutral95,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp),
                            ) {
                                session.terminalOutput.forEach { line ->
                                    Text(
                                        text = line,
                                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                                        color = OpenChatPalette.Neutral95,
                                    )
                                }
                            }
                        }

                        // Sandbox Command Approval (`Allow Codex to run this command?`)
                        if (session.pendingCommandApproval != null) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    Text(
                                        text = "Allow Codex to run this command?",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                    Text(
                                        text = session.pendingCommandApproval,
                                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(onClick = { controller.approveCodexCommand(session.id) }) {
                                            Text("Approve")
                                        }
                                        OutlinedButton(onClick = { controller.approveCodexCommand(session.id) }) {
                                            Text("Always allow")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Remote Composer Help (`/help`, `/status`, `/goal`, `/plan`, `/fast`, `/ultrafast`)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Remote Composer Commands (strings.xml#L1318-L1320)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Type $ for skills and MCP servers, or @ for plugins. /help opens this local help sheet. /status shows Remote thread details. /goal sets or edits the thread goal. /plan toggles Plan Mode for the next Remote turn. /fast and /ultrafast toggle supported response speeds.",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    OutlinedTextField(
                        value = remoteCommandInput,
                        onValueChange = { remoteCommandInput = it },
                        label = { Text("Tell Codex what to do") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
fun OrbitTibroScreen(
    snapshot: AppSnapshot,
    controller: FakeStateController,
    onNavigateToVoice: () -> Unit,
) {
    var selectedTab by remember { mutableStateOf("Dots") }
    var newRuleInput by remember { mutableStateOf("") }
    val dot = snapshot.orbitDots.first()

    var selectedColor by remember(dot.avatarColor) { mutableStateOf(dot.avatarColor) }
    var selectedShape by remember(dot.avatarShape) { mutableStateOf(dot.avatarShape) }
    var selectedEyes by remember(dot.avatarEyes) { mutableStateOf(dot.avatarEyes) }
    var selectedGlasses by remember(dot.avatarGlasses) { mutableStateOf(dot.avatarGlasses) }

    StateSimulationHost(
        state = snapshot.uiSimState,
        loadingTitle = "Getting your Dot ready...",
        emptyTitle = "No Dots configured yet",
        emptySubtitle = "Dots are remarkably capable always-on agents.",
        errorTitle = "We couldn't set up your Dot",
        errorSubtitle = "Reconnect to continue. Orbit received the update.",
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
                title = "Orbit / Tibro: Always-On Dots",
                subtitle = "Reconstructed from TibroPrimaryRoute, Dots (wbv), Ideas (xbv), and You (zbv).",
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Dots", "Ideas", "You").forEach { tab ->
                    FilterChip(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        label = { Text(tab) },
                    )
                }
            }

            when (selectedTab) {
                "Dots" -> {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.fillMaxWidth(),
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
                                Column {
                                    Text(
                                        text = dot.name,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(
                                        text = "${dot.status} | Avatar: ${dot.avatarColor} ${dot.avatarShape} (${dot.avatarEyes}, ${dot.avatarGlasses})",
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                }
                                StatusBadge(text = "Monitoring")
                            }
                            Text(
                                text = "Email: ${dot.emailAddress} | Texting: ${dot.phoneNumber}",
                                style = MaterialTheme.typography.labelMedium,
                            )
                            Text(
                                text = "Connected apps: ${dot.connectedApps.joinToString(", ")}",
                                style = MaterialTheme.typography.labelSmall,
                            )
                            Button(onClick = onNavigateToVoice) {
                                DsIcon(
                                    drawableRes = OpenChatIcons.VoiceButton,
                                    contentDescription = null,
                                    size = 18.dp,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Call with your dot")
                            }
                        }
                    }

                    // Custom Avatar Customizer (`strings.xml#L4319-L4334`)
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = "Edit name and avatar (Character preview)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                listOf("Purple", "Blue", "Green", "Orange", "Gray").forEach { col ->
                                    FilterChip(
                                        selected = selectedColor == col,
                                        onClick = { selectedColor = col },
                                        label = { Text("Color: $col", style = MaterialTheme.typography.labelSmall) },
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                listOf("Orbital", "Hex", "Diamond", "Capsule").forEach { shp ->
                                    FilterChip(
                                        selected = selectedShape == shp,
                                        onClick = { selectedShape = shp },
                                        label = { Text("Shape: $shp", style = MaterialTheme.typography.labelSmall) },
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                listOf("None", "Round Wire", "Visor", "Monocle").forEach { gls ->
                                    FilterChip(
                                        selected = selectedGlasses == gls,
                                        onClick = { selectedGlasses = gls },
                                        label = { Text("Glasses: $gls", style = MaterialTheme.typography.labelSmall) },
                                    )
                                }
                            }
                            Button(
                                onClick = {
                                    controller.updateOrbitDotAvatar(
                                        color = selectedColor,
                                        shape = selectedShape,
                                        eyes = selectedEyes,
                                        glasses = selectedGlasses,
                                    )
                                },
                            ) {
                                Text("Save Avatar")
                            }
                        }
                    }
                }

                "Ideas" -> {
                    SectionHeader(
                        title = "Ideas Feed (xbv.java)",
                        subtitle = "Proactive tasks synthesized from your calendar, inbox, and repositories.",
                    )
                    dot.ideasFeed.forEach { idea ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                Text(idea, style = MaterialTheme.typography.bodyMedium)
                                StatusBadge(text = "Proactive suggestion")
                            }
                        }
                    }
                }

                "You" -> {
                    SectionHeader(
                        title = "Custom Rules & Approval Policies (zbv.java)",
                        subtitle = "Your dot knows when to take action, and when to ask for approval.",
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        OutlinedTextField(
                            value = newRuleInput,
                            onValueChange = { newRuleInput = it },
                            placeholder = { Text("When Nova Dot wants to... e.g. write an email for me") },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                        )
                        Button(
                            onClick = {
                                controller.addOrbitDotRule(newRuleInput)
                                newRuleInput = ""
                            },
                        ) {
                            Text("Save rule")
                        }
                    }
                    dot.customRules.forEach { rule ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = rule,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(12.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalpicoSocialScreen(
    snapshot: AppSnapshot,
    controller: FakeStateController,
) {
    var selectedFilter by remember { mutableStateOf("Inbox") }

    StateSimulationHost(
        state = snapshot.uiSimState,
        loadingTitle = "Loading Calpico group chats...",
        emptyTitle = "You have no chat requests",
        emptySubtitle = "Share responses, images, and creations with friends, then keep the conversation going.",
        errorTitle = "Unable to load messages",
        errorSubtitle = "Check your connection and try again.",
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
                title = "Open Chat with Friends (Calpico)",
                subtitle = "Direct messages and group chats with magic links and custom assistant personalities (uyu, e0v, i0v).",
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Inbox", "Unread", "Requests").forEach { tab ->
                    FilterChip(
                        selected = selectedFilter == tab,
                        onClick = { selectedFilter = tab },
                        label = { Text(tab) },
                    )
                }
            }

            val threads = snapshot.calpicoThreads.filter {
                when (selectedFilter) {
                    "Unread" -> it.unreadCount > 0 && !it.isRequest
                    "Requests" -> it.isRequest
                    else -> !it.isRequest
                }
            }

            threads.forEach { thread ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
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
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = thread.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Text(
                                    text = "Members: ${thread.participants.joinToString(", ")}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            if (thread.unreadCount > 0) {
                                StatusBadge(text = "${thread.unreadCount} Unread")
                            } else {
                                StatusBadge(text = thread.updatedRelative)
                            }
                        }
                        Text(text = thread.lastMessage, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "Assistant respond mode: ${thread.assistantRespondMode}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        if (thread.isRequest) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = { controller.acceptCalpicoRequest(thread.id) }) {
                                    Text("Accept request")
                                }
                                OutlinedButton(onClick = {}) {
                                    Text("Decline request")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FinanceAndVerticalsScreen(
    snapshot: AppSnapshot,
    controller: FakeStateController,
) {
    var cardLast4Input by remember { mutableStateOf("9012") }
    var selectedTryOnMode by remember { mutableStateOf("Selfie reference photo") }
    var purchaseApproved by remember { mutableStateOf(false) }

    StateSimulationHost(
        state = snapshot.uiSimState,
        loadingTitle = "Loading Finances dashboard, Wallet & Shopping...",
        emptyTitle = "No dashboard yet",
        emptySubtitle = "Ask a finance question or connect accounts to get started.",
        errorTitle = "We couldn't load this Finance content.",
        errorSubtitle = "Last sync failed. Check your connection and try again.",
        onRetry = { controller.setUiSimState(UiSimState.SUCCESS) },
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Finances Dashboard (`defpackage/f7v.java`)
            SectionHeader(
                title = "Finances & Net Worth Dashboard",
                subtitle = "Securely connect your financial accounts and get personalized insights (f7v.java).",
                actionLabel = if (snapshot.isFinanceBalanceHidden) "Show balances" else "Hide balances",
                onActionClick = { controller.toggleFinanceBalanceHidden() },
            )

            snapshot.financeAccounts.forEach { acc ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    modifier = Modifier.fillMaxWidth(),
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
                                text = "${acc.institution} | ${acc.accountName}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = "${acc.category} (${acc.sharePercentage}% of category) | ${acc.status}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Text(
                            text = if (snapshot.isFinanceBalanceHidden) "Balance hidden" else acc.balanceFormatted,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            // Monthly Spending Breakdown (`strings.xml#L3242-L3251`)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "Monthly Spending Breakdown ($3,800 total)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text("* Rent and utilities: $1,300", style = MaterialTheme.typography.bodySmall)
                    Text("* Groceries: $1,200", style = MaterialTheme.typography.bodySmall)
                    Text("* Subscriptions: $800", style = MaterialTheme.typography.bodySmall)
                    Text("* Dining: $500", style = MaterialTheme.typography.bodySmall)
                }
            }

            HorizontalDivider()

            // Wallet & Purchase Approval (`defpackage/xpv.java`, `WalletVgsCardScanActivity`)
            SectionHeader(
                title = "Wallet & Secure Checkout Approval",
                subtitle = "Manage payment methods, scan cards, and approve in-chat purchases (xpv.java).",
            )

            snapshot.walletCards.forEach { card ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text(
                                text = "${card.brand} **** ${card.last4}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = "${card.cardholder} | Expires ${card.expiry}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        if (card.isDefault) {
                            StatusBadge(text = "Default")
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = cardLast4Input,
                    onValueChange = { cardLast4Input = it },
                    label = { Text("Last 4 digits (Scan card simulator)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                Button(onClick = { controller.addWalletCard("Visa", cardLast4Input, "08/30") }) {
                    Text("Add card")
                }
            }

            HorizontalDivider()

            // Shopping Hub & Virtual Try-On (`defpackage/skv.java`, `strings.xml#L7732-L7752`)
            SectionHeader(
                title = "Shopping Hub & Virtual Try-On",
                subtitle = "Compare products, inspect tax/shipping breakdown, and preview outfits with reference photos.",
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Selfie reference photo", "Full-body reference photo").forEach { mode ->
                    FilterChip(
                        selected = selectedTryOnMode == mode,
                        onClick = { selectedTryOnMode = mode },
                        label = { Text(mode, style = MaterialTheme.typography.labelSmall) },
                    )
                }
            }

            snapshot.shoppingProducts.forEach { prod ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = prod.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f),
                            )
                            StatusBadge(text = prod.price)
                        }
                        Text(
                            text = "${prod.merchant} | ${prod.rating}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = prod.taxAndShipping,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "Why you might like this: ${prod.whyYouMightLike}",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            text = "What people are saying: ${prod.reviewHighlights}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { purchaseApproved = true }) {
                                Text(if (purchaseApproved) "Purchase approved" else "Approve payment")
                            }
                            OutlinedButton(onClick = {}) {
                                Text("Try on ($selectedTryOnMode)")
                            }
                        }
                    }
                }
            }

            HorizontalDivider()

            // Live Sports (`/football`)
            SectionHeader(
                title = "Live Sports & Conference Scores (/football)",
                subtitle = "Real-time match scoreboards and tactical summaries.",
            )

            snapshot.sportsMatches.forEach { match ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = match.competition,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            StatusBadge(text = match.status)
                        }
                        Text(
                            text = "${match.homeTeam} ${match.homeScore} - ${match.awayScore} ${match.awayTeam}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(text = match.summary, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun LockedChatsAndSafetyScreen(
    snapshot: AppSnapshot,
    controller: FakeStateController,
    onNavigateToChat: () -> Unit,
) {
    var pinInput by remember { mutableStateOf("2468") }
    var pinError by remember { mutableStateOf(false) }

    StateSimulationHost(
        state = snapshot.uiSimState,
        loadingTitle = "Verifying Account PIN & safety settings...",
        emptyTitle = "No locked chats yet",
        emptySubtitle = "Create a PIN to lock and hide the chats you choose.",
        errorTitle = "Locked chats are temporarily unavailable. Try again.",
        errorSubtitle = "Device unlock is unavailable. Enter your account PIN instead.",
        onRetry = { controller.setUiSimState(UiSimState.SUCCESS) },
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Locked Chats Account PIN Vault (`nbv.java`, `qbv.java`)
            SectionHeader(
                title = "Locked Chats & Account PIN Vault",
                subtitle = "Once locked, new messages in that chat won't contribute to general memory (nbv, qbv).",
            )

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = if (snapshot.isLockedChatsUnlocked) {
                            "Vault Status: Unlocked"
                        } else {
                            "Vault Status: Locked (Default PIN: 2468)"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        OutlinedTextField(
                            value = pinInput,
                            onValueChange = {
                                pinInput = it
                                pinError = false
                            },
                            label = { Text("Enter Account PIN") },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                        )
                        Button(
                            onClick = {
                                val ok = controller.unlockLockedChatsWithPin(pinInput)
                                pinError = !ok
                            },
                        ) {
                            Text("Unlock")
                        }
                    }
                    if (pinError) {
                        Text(
                            text = "That PIN is incorrect. Try again.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }

                    if (snapshot.isLockedChatsUnlocked) {
                        snapshot.conversations.filter { it.isLocked }.forEach { lockedThread ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        controller.selectConversation(lockedThread.id)
                                        onNavigateToChat()
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
                                            text = lockedThread.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.SemiBold,
                                        )
                                        Text(
                                            text = "Protected by Account PIN | Tap to open in Chat",
                                            style = MaterialTheme.typography.labelSmall,
                                        )
                                    }
                                    StatusBadge(text = "LOCKED")
                                }
                            }
                        }
                    }
                }
            }

            HorizontalDivider()

            // Lockdown Mode & Daybreak Cybersecurity Access (`strings.xml#L2220-L2273`, `L5973-L5980`)
            SectionHeader(
                title = "Lockdown Mode & Daybreak Cybersecurity Access",
                subtitle = "Protect sensitive data from prompt-injection attacks or enable authorized cyber research.",
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Lockdown Mode", style = MaterialTheme.typography.titleSmall)
                    Text(
                        "More protection, limited features. Limits live web browsing and external connectors.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Switch(
                    checked = snapshot.isLockdownModeEnabled,
                    onCheckedChange = { controller.toggleLockdownMode() },
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Enable Daybreak (Authorized Security Work)", style = MaterialTheme.typography.titleSmall)
                    Text(
                        "Broader access for verified cybersecurity researchers.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Switch(
                    checked = snapshot.isDaybreakEnabled,
                    onCheckedChange = { controller.toggleDaybreak() },
                )
            }

            HorizontalDivider()

            // Parental Controls, Quiet Hours & Study Hours (`hpz.java`, `lpz.java`)
            SectionHeader(
                title = "Parental Controls, Quiet Hours & Study Hours",
                subtitle = "Family linking, time management schedules, and teen safeguards.",
            )

            snapshot.familyMembers.forEach { member ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = "${member.name} (${member.role})",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                            )
                            StatusBadge(text = member.status)
                        }
                        Text("Quiet hours: ${member.quietHours}", style = MaterialTheme.typography.bodySmall)
                        Text("Study hours: ${member.studyHours}", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = "Reduce sensitive content: ${if (member.reduceSensitiveContent) "Enabled" else "Disabled"}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }

            HorizontalDivider()

            // Trusted Contact (`ad01.java`, `ed01.java`, `kd01.java`, `oe01.java`)
            SectionHeader(
                title = "Trusted Contact Safety Network",
                subtitle = "Having a trusted contact can make it easier to get support from someone who knows you well.",
            )

            snapshot.trustedContacts.forEach { tc ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = tc.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                            )
                            StatusBadge(text = tc.status)
                        }
                        Text("${tc.email} | ${tc.phone}", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = "What to expect: We won't share your chat details or transcripts, only a general check-in prompt if needed.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExperimentInspectorScreen(
    snapshot: AppSnapshot,
    controller: FakeStateController,
    onJumpToDestination: (OpenChatDestination) -> Unit,
) {
    var flagSearch by remember { mutableStateOf("") }
    var showIconCatalog by remember { mutableStateOf(true) }

    val filteredFlags = snapshot.featureFlags.filter {
        flagSearch.isBlank() ||
            it.title.contains(flagSearch, ignoreCase = true) ||
            it.key.contains(flagSearch, ignoreCase = true) ||
            it.category.contains(flagSearch, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SectionHeader(
            title = "Experiment Override & In-App Inspector (f6v.java)",
            subtitle = "Toggle feature flags, switch UI states (Loading/Empty/Error/Success), and jump to any screen.",
        )

        // Global UI State & Tier Simulator
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Runtime UI State & Plan Override",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    UiSimState.entries.forEach { st ->
                        FilterChip(
                            selected = snapshot.uiSimState == st,
                            onClick = { controller.setUiSimState(st) },
                            label = { Text("State: ${st.label}") },
                        )
                    }
                }
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    SubscriptionTier.entries.forEach { tier ->
                        FilterChip(
                            selected = snapshot.subscriptionTier == tier,
                            onClick = { controller.setSubscriptionTier(tier) },
                            label = { Text("Plan: ${tier.label}") },
                        )
                    }
                }
            }
        }

        // Direct Route Jumper
        SectionHeader(
            title = "All Reconstructed Destinations (${OpenChatDestination.entries.size} Routes)",
            subtitle = "Tap any route below to inspect its screen and test its 4 UI states.",
        )

        OpenChatDestination.entries.chunked(2).forEach { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                pair.forEach { dest ->
                    OutlinedButton(
                        onClick = { onJumpToDestination(dest) },
                        modifier = Modifier.weight(1f),
                    ) {
                        DsIcon(drawableRes = dest.iconRes, contentDescription = null, size = 16.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${dest.phaseBadge}: ${dest.title}",
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                        )
                    }
                }
            }
        }

        HorizontalDivider()

        // Feature Flags Registry
        SectionHeader(
            title = "Feature Flags Registry (${snapshot.featureFlags.count { it.enabled }}/${snapshot.featureFlags.size} Enabled)",
            subtitle = "All hidden/experimental flags default to true and update the UI immediately.",
            actionLabel = "Enable All",
            onActionClick = { controller.setAllFeatureFlags(true) },
        )

        OutlinedTextField(
            value = flagSearch,
            onValueChange = { flagSearch = it },
            placeholder = { Text("Filter feature flags by key, title, or category") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        filteredFlags.forEach { flag ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            StatusBadge(text = flag.category)
                            Text(
                                text = flag.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                        Text(
                            text = flag.description,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            text = "Key: ${flag.key} | Ref: ${flag.sourceRef}",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Switch(
                        checked = flag.enabled,
                        onCheckedChange = { controller.toggleFeatureFlag(flag.key) },
                    )
                }
            }
        }

        HorizontalDivider()

        // Design System Token & 48-Icon Vector Catalog
        SectionHeader(
            title = "Design System Tokens & 48 Vector Drawables",
            subtitle = "Spring damping=${OpenChatMotion.DEFAULT_SPATIAL_DAMPING}, stiffness=${OpenChatMotion.DEFAULT_SPATIAL_STIFFNESS} | Max width=${OpenChatDimens.ConstraintWidthMax.value.toInt()}dp",
            actionLabel = if (showIconCatalog) "Hide Icons" else "Show Icons",
            onActionClick = { showIconCatalog = !showIconCatalog },
        )

        if (showIconCatalog) {
            OpenChatIcons.AllCatalog.chunked(2).forEach { rowIcons ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    rowIcons.forEach { entry ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                            modifier = Modifier.weight(1f),
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                DsIcon(
                                    drawableRes = entry.drawableRes,
                                    contentDescription = entry.label,
                                    size = 20.dp,
                                )
                                Column {
                                    Text(
                                        text = entry.label,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Medium,
                                    )
                                    Text(
                                        text = entry.id,
                                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
