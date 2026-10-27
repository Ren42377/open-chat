package io.openchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
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
import io.openchat.ui.theme.OpenChatMotion
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenChatApp(
    controller: FakeStateController,
    initialDestination: OpenChatDestination = OpenChatDestination.CONVERSATION,
) {
    val snapshot by controller.state.collectAsState()
    var currentDestination by remember { mutableStateOf(initialDestination) }
    var showDebugSheet by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    OpenChatTheme(
        themeMode = snapshot.themeMode,
        accentColor = snapshot.accentColor,
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = currentDestination != OpenChatDestination.VOICE_CALL,
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
                containerColor = MaterialTheme.colorScheme.background,
                topBar = {
                    val usesCustomScreenChrome = currentDestination == OpenChatDestination.CONVERSATION ||
                        currentDestination == OpenChatDestination.VOICE_CALL

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding(),
                    ) {
                        if (!usesCustomScreenChrome) {
                            TopScreenHeader(
                                title = currentDestination.title,
                                subtitle = null,
                                showBackArrow = true,
                                onNavigateBack = {
                                    currentDestination = OpenChatDestination.CONVERSATION
                                },
                                onOpenDrawer = {
                                    coroutineScope.launch { drawerState.open() }
                                },
                                onOpenInspector = {
                                    showDebugSheet = true
                                },
                                trailingActionIcon = OpenChatIcons.ComposeNewChat,
                                trailingActionDescription = "New chat",
                                onTrailingAction = {
                                    controller.startNewChat()
                                    currentDestination = OpenChatDestination.CONVERSATION
                                },
                            )
                        }

                        AnimatedVisibility(visible = snapshot.lastStatusBanner != null) {
                            val bannerText = snapshot.lastStatusBanner.orEmpty()
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Surface(
                                    color = MaterialTheme.colorScheme.inverseSurface,
                                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                                    shape = RoundedCornerShape(24.dp),
                                    shadowElevation = 2.dp,
                                    modifier = Modifier.clickable { controller.clearStatusBanner() },
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Text(
                                            text = bannerText,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Medium,
                                        )
                                        IconButton(
                                            onClick = { controller.clearStatusBanner() },
                                            modifier = Modifier.height(20.dp),
                                        ) {
                                            DsIcon(
                                                drawableRes = OpenChatIcons.CloseX,
                                                contentDescription = "Dismiss notification",
                                                size = 14.dp,
                                                tint = MaterialTheme.colorScheme.inverseOnSurface,
                                            )
                                        }
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
                    AnimatedContent(
                        targetState = currentDestination,
                        transitionSpec = { OpenChatMotion.destinationMorph() },
                        label = "DestinationSpatialTransition",
                    ) { destination ->
                        when (destination) {
                            OpenChatDestination.CONVERSATION -> ConversationScreen(
                                snapshot = snapshot,
                                controller = controller,
                                onNavigate = { currentDestination = it },
                                onOpenDrawer = {
                                    coroutineScope.launch { drawerState.open() }
                                },
                                onOpenDebugSheet = {
                                    showDebugSheet = true
                                },
                            )

                            OpenChatDestination.VOICE_CALL -> VoiceCallScreen(
                                snapshot = snapshot,
                                controller = controller,
                                onEndCall = {
                                    currentDestination = OpenChatDestination.CONVERSATION
                                },
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
                                onJumpToDestination = { nextDest ->
                                    currentDestination = nextDest
                                },
                            )
                        }
                    }
                }
            }

            if (showDebugSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showDebugSheet = false },
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    containerColor = MaterialTheme.colorScheme.surface,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = "Developer State & Tier Simulator",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = "Hidden from the main UI. Triggered by long-pressing any screen title or via the menu.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        QuickStateSwitcherBar(
                            currentState = snapshot.uiSimState,
                            currentTier = snapshot.subscriptionTier,
                            onSelectState = { state -> controller.setUiSimState(state) },
                            onSelectTier = { tier -> controller.setSubscriptionTier(tier) },
                            onOpenFullInspector = {
                                showDebugSheet = false
                                currentDestination = OpenChatDestination.EXPERIMENT_INSPECTOR
                            },
                        )
                        Button(
                            onClick = {
                                showDebugSheet = false
                                currentDestination = OpenChatDestination.EXPERIMENT_INSPECTOR
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Open Full Experiment Override & Icon Catalog")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}
