package io.openchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.openchat.data.mock.FakeStateController
import io.openchat.navigation.OpenChatDestination
import io.openchat.ui.components.DsIcon
import io.openchat.ui.components.QuickStateSwitcherBar
import io.openchat.ui.components.TopScreenHeader
import io.openchat.ui.screens.ArtifactLibraryScreen
import io.openchat.ui.screens.AuthAndSubscriptionScreen
import io.openchat.ui.screens.CalpicoSocialScreen
import io.openchat.ui.screens.CameraSketchScreen
import io.openchat.ui.screens.CodexRemoteScreen
import io.openchat.ui.screens.ConversationScreen
import io.openchat.ui.screens.DiscoveryHubScreen
import io.openchat.ui.screens.ExperimentInspectorScreen
import io.openchat.ui.screens.FinanceAndVerticalsScreen
import io.openchat.ui.screens.LockedChatsAndSafetyScreen
import io.openchat.ui.screens.NavigationDrawerContent
import io.openchat.ui.screens.OrbitTibroScreen
import io.openchat.ui.screens.SettingsAndProfileScreen
import io.openchat.ui.screens.SpacesAndTasksScreen
import io.openchat.ui.screens.VoiceCallScreen
import io.openchat.ui.theme.OpenChatIcons
import io.openchat.ui.theme.OpenChatTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val initialDestination = resolveDestinationFromDeepLink(intent?.data?.path)
        setContent {
            val controller = remember { FakeStateController() }
            OpenChatApp(
                controller = controller,
                initialDestination = initialDestination,
            )
        }
    }
}

fun resolveDestinationFromDeepLink(path: String?): OpenChatDestination {
    if (path.isNullOrBlank()) return OpenChatDestination.CONVERSATION
    val normalized = path.lowercase()
    return when {
        normalized.startsWith("/voice") -> OpenChatDestination.VOICE_CALL
        normalized.startsWith("/sketch") -> OpenChatDestination.CAMERA_SKETCH
        normalized.startsWith("/library") -> OpenChatDestination.ARTIFACT_LIBRARY
        normalized.startsWith("/discovery-hub") || normalized.startsWith("/gpts") || normalized.startsWith("/g/") ->
            OpenChatDestination.DISCOVERY_HUB
        normalized.startsWith("/spaces") || normalized.startsWith("/scheduled") || normalized.startsWith("/tasks") ->
            OpenChatDestination.SPACES_AND_TASKS
        normalized.startsWith("/settings") || normalized.startsWith("/accountsecurity") ->
            OpenChatDestination.SETTINGS_AND_PROFILE
        normalized.startsWith("/subscription") || normalized.startsWith("/pricing") || normalized.startsWith("/auth") ->
            OpenChatDestination.AUTH_AND_SUBSCRIPTION
        normalized.startsWith("/codex") -> OpenChatDestination.CODEX_REMOTE
        normalized.startsWith("/dots") -> OpenChatDestination.ORBIT_TIBRO
        normalized.startsWith("/calpico") -> OpenChatDestination.CALPICO_SOCIAL
        normalized.startsWith("/finances") || normalized.startsWith("/shopping") || normalized.startsWith("/football") ->
            OpenChatDestination.FINANCE_AND_VERTICALS
        normalized.startsWith("/parentalcontrols") || normalized.startsWith("/locked") ->
            OpenChatDestination.LOCKED_CHATS_AND_SAFETY
        normalized.startsWith("/experiments") -> OpenChatDestination.EXPERIMENT_INSPECTOR
        else -> OpenChatDestination.CONVERSATION
    }
}

@Composable
fun OpenChatApp(
    controller: FakeStateController,
    initialDestination: OpenChatDestination = OpenChatDestination.CONVERSATION,
) {
    val snapshot by controller.snapshot.collectAsState()
    var currentDestination by remember { mutableStateOf(initialDestination) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    OpenChatTheme(
        themeMode = snapshot.themeMode,
        accentColor = snapshot.accentColor,
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                NavigationDrawerContent(
                    snapshot = snapshot,
                    currentDestination = currentDestination,
                    controller = controller,
                    onSelectDestination = { destination ->
                        currentDestination = destination
                    },
                    onCloseDrawer = {
                        coroutineScope.launch { drawerState.close() }
                    },
                )
            },
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding(),
                    ) {
                        val activeConversation = snapshot.conversations.find {
                            it.id == snapshot.selectedConversationId
                        }
                        val headerTitle = if (currentDestination == OpenChatDestination.CONVERSATION) {
                            activeConversation?.title ?: currentDestination.title
                        } else {
                            currentDestination.title
                        }
                        val headerSubtitle = if (currentDestination == OpenChatDestination.CONVERSATION) {
                            "${snapshot.selectedModel.displayName} | ${snapshot.activeMode.label} | ${snapshot.subscriptionTier.label}"
                        } else {
                            "${currentDestination.subtitle} (${currentDestination.deepLinkPath})"
                        }

                        TopScreenHeader(
                            title = headerTitle,
                            subtitle = headerSubtitle,
                            onOpenDrawer = {
                                coroutineScope.launch { drawerState.open() }
                            },
                            onOpenInspector = {
                                currentDestination = OpenChatDestination.EXPERIMENT_INSPECTOR
                            },
                            trailingActionIcon = if (currentDestination == OpenChatDestination.CONVERSATION) {
                                OpenChatIcons.Compose
                            } else {
                                OpenChatIcons.ChatBubble
                            },
                            trailingActionDescription = if (currentDestination == OpenChatDestination.CONVERSATION) {
                                "New chat"
                            } else {
                                "Back to chat"
                            },
                            onTrailingAction = {
                                if (currentDestination == OpenChatDestination.CONVERSATION) {
                                    controller.startNewConversation()
                                } else {
                                    currentDestination = OpenChatDestination.CONVERSATION
                                }
                            },
                        )

                        QuickStateSwitcherBar(
                            currentState = snapshot.uiSimState,
                            currentTier = snapshot.subscriptionTier,
                            onSelectState = { state -> controller.setUiSimState(state) },
                            onSelectTier = { tier -> controller.setSubscriptionTier(tier) },
                            onOpenFullInspector = {
                                currentDestination = OpenChatDestination.EXPERIMENT_INSPECTOR
                            },
                        )

                        AnimatedVisibility(visible = snapshot.statusBannerMessage != null) {
                            val bannerText = snapshot.statusBannerMessage.orEmpty()
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = bannerText,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.weight(1f),
                                    )
                                    IconButton(onClick = { controller.dismissStatusBanner() }) {
                                        DsIcon(
                                            drawableRes = OpenChatIcons.Checkmark,
                                            contentDescription = "Dismiss notification",
                                            size = 16.dp,
                                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    when (currentDestination) {
                        OpenChatDestination.CONVERSATION -> ConversationScreen(
                            snapshot = snapshot,
                            controller = controller,
                            onNavigate = { currentDestination = it },
                        )

                        OpenChatDestination.VOICE_CALL -> VoiceCallScreen(
                            snapshot = snapshot,
                            controller = controller,
                        )

                        OpenChatDestination.CAMERA_SKETCH -> CameraSketchScreen(
                            snapshot = snapshot,
                            controller = controller,
                            onNavigateBackToChat = {
                                currentDestination = OpenChatDestination.CONVERSATION
                            },
                        )

                        OpenChatDestination.ARTIFACT_LIBRARY -> ArtifactLibraryScreen(
                            snapshot = snapshot,
                            controller = controller,
                            onNavigateToChat = {
                                currentDestination = OpenChatDestination.CONVERSATION
                            },
                        )

                        OpenChatDestination.DISCOVERY_HUB -> DiscoveryHubScreen(
                            snapshot = snapshot,
                            controller = controller,
                            onNavigateToChat = {
                                currentDestination = OpenChatDestination.CONVERSATION
                            },
                        )

                        OpenChatDestination.SPACES_AND_TASKS -> SpacesAndTasksScreen(
                            snapshot = snapshot,
                            controller = controller,
                            onNavigateToChat = {
                                currentDestination = OpenChatDestination.CONVERSATION
                            },
                        )

                        OpenChatDestination.SETTINGS_AND_PROFILE -> SettingsAndProfileScreen(
                            snapshot = snapshot,
                            controller = controller,
                        )

                        OpenChatDestination.AUTH_AND_SUBSCRIPTION -> AuthAndSubscriptionScreen(
                            snapshot = snapshot,
                            controller = controller,
                        )

                        OpenChatDestination.CODEX_REMOTE -> CodexRemoteScreen(
                            snapshot = snapshot,
                            controller = controller,
                        )

                        OpenChatDestination.ORBIT_TIBRO -> OrbitTibroScreen(
                            snapshot = snapshot,
                            controller = controller,
                            onNavigateToVoice = {
                                currentDestination = OpenChatDestination.VOICE_CALL
                            },
                        )

                        OpenChatDestination.CALPICO_SOCIAL -> CalpicoSocialScreen(
                            snapshot = snapshot,
                            controller = controller,
                        )

                        OpenChatDestination.FINANCE_AND_VERTICALS -> FinanceAndVerticalsScreen(
                            snapshot = snapshot,
                            controller = controller,
                        )

                        OpenChatDestination.LOCKED_CHATS_AND_SAFETY -> LockedChatsAndSafetyScreen(
                            snapshot = snapshot,
                            controller = controller,
                            onNavigateToChat = {
                                currentDestination = OpenChatDestination.CONVERSATION
                            },
                        )

                        OpenChatDestination.EXPERIMENT_INSPECTOR -> ExperimentInspectorScreen(
                            snapshot = snapshot,
                            controller = controller,
                            onJumpToDestination = { destination ->
                                currentDestination = destination
                            },
                        )
                    }
                }
            }
        }
    }
}
