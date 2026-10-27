package io.openchat

import io.openchat.data.mock.AccentColorOption
import io.openchat.data.mock.FakeStateController
import io.openchat.data.mock.LocalMockRepository
import io.openchat.data.mock.ReasoningEffort
import io.openchat.data.mock.SubscriptionTier
import io.openchat.data.mock.ThemeMode
import io.openchat.data.mock.UiSimState
import io.openchat.navigation.OpenChatDestination
import io.openchat.ui.theme.OpenChatIcons
import io.openchat.ui.theme.OpenChatMotion
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ApplicationConfigTest {
    @Test
    fun packageIdentifierMatchesExpectedNamespace() {
        assertEquals("io.openchat", MainActivity::class.java.packageName)
    }

    @Test
    fun allFeatureFlagsDefaultToEnabledAndCanToggle() {
        val controller = FakeStateController()
        val initialFlags = controller.state.value.featureFlags
        assertEquals(24, initialFlags.size)
        assertTrue(initialFlags.all { it.enabled })

        val targetFlagKey = initialFlags.first().key
        controller.toggleFeatureFlag(targetFlagKey)
        assertFalse(controller.state.value.isFlagEnabled(targetFlagKey))

        controller.setAllFeatureFlags(true)
        assertTrue(controller.state.value.featureFlags.all { it.enabled })
    }

    @Test
    fun uiSimStateAndSubscriptionTierTransitionsWorkDeterministically() {
        val controller = FakeStateController()
        assertEquals(UiSimState.SUCCESS, controller.state.value.uiSimState)

        for (simState in UiSimState.entries) {
            controller.setUiSimState(simState)
            assertEquals(simState, controller.state.value.uiSimState)
        }

        for (tier in SubscriptionTier.entries) {
            controller.setSubscriptionTier(tier)
            assertEquals(tier, controller.state.value.subscriptionTier)
        }

        for (theme in ThemeMode.entries) {
            controller.setThemeMode(theme)
            assertEquals(theme, controller.state.value.themeMode)
        }

        for (accent in AccentColorOption.entries) {
            controller.setAccentColor(accent)
            assertEquals(accent, controller.state.value.accentColor)
        }
    }

    @Test
    fun conversationMessagingAndVariantSelectionMutateState() {
        val controller = FakeStateController()
        controller.toggleDeepResearch()
        controller.selectEffort(ReasoningEffort.HIGH)

        val beforeCount = controller.state.value.activeConversation!!.messages.size

        controller.sendUserMessage("Verify clean-room architecture and offline mock repository")

        val activeThread = controller.state.value.activeConversation!!
        assertEquals(beforeCount + 2, activeThread.messages.size)

        controller.selectMessageVariant(
            messageId = "msg-2",
            variantIndex = 1,
        )
        val updatedMessage = controller.state.value.activeConversation!!
            .messages.first { it.id == "msg-2" }
        assertEquals(1, updatedMessage.selectedVariantIndex)
    }

    @Test
    fun lockedChatsPinAndCodexRemoteActionsUpdateSnapshot() {
        val controller = FakeStateController()
        assertFalse(controller.state.value.isLockedChatsUnlocked)

        val wrongAttempt = controller.unlockLockedChatsWithPin("0000")
        assertFalse(wrongAttempt)
        assertFalse(controller.state.value.isLockedChatsUnlocked)

        val validAttempt = controller.unlockLockedChatsWithPin("2468")
        assertTrue(validAttempt)
        assertTrue(controller.state.value.isLockedChatsUnlocked)

        val sessionWithApproval = controller.state.value.codexSessions.first { it.pendingCommandApproval != null }
        controller.approveCodexCommand(sessionWithApproval.id)
        val updatedSession = controller.state.value.codexSessions.first { it.id == sessionWithApproval.id }
        assertNull(updatedSession.pendingCommandApproval)
        assertNotNull(controller.state.value.lastStatusBanner)
    }

    @Test
    fun deepLinkResolverMapsAllFourteenDestinations() {
        assertEquals(OpenChatDestination.CONVERSATION, resolveDestinationFromDeepLink(null))
        assertEquals(OpenChatDestination.CONVERSATION, resolveDestinationFromDeepLink("/c/123"))
        assertEquals(OpenChatDestination.VOICE_CALL, resolveDestinationFromDeepLink("/voice"))
        assertEquals(OpenChatDestination.CAMERA_SKETCH, resolveDestinationFromDeepLink("/sketch"))
        assertEquals(OpenChatDestination.ARTIFACT_LIBRARY, resolveDestinationFromDeepLink("/library"))
        assertEquals(OpenChatDestination.DISCOVERY_HUB, resolveDestinationFromDeepLink("/discovery-hub"))
        assertEquals(OpenChatDestination.SPACES_AND_TASKS, resolveDestinationFromDeepLink("/spaces/abc"))
        assertEquals(OpenChatDestination.SPACES_AND_TASKS, resolveDestinationFromDeepLink("/scheduled"))
        assertEquals(OpenChatDestination.SETTINGS_AND_PROFILE, resolveDestinationFromDeepLink("/settings"))
        assertEquals(OpenChatDestination.AUTH_AND_SUBSCRIPTION, resolveDestinationFromDeepLink("/subscription"))
        assertEquals(OpenChatDestination.CODEX_REMOTE, resolveDestinationFromDeepLink("/codex/remote-next"))
        assertEquals(OpenChatDestination.ORBIT_TIBRO, resolveDestinationFromDeepLink("/dots"))
        assertEquals(OpenChatDestination.CALPICO_SOCIAL, resolveDestinationFromDeepLink("/calpico"))
        assertEquals(OpenChatDestination.FINANCE_AND_VERTICALS, resolveDestinationFromDeepLink("/finances"))
        assertEquals(OpenChatDestination.FINANCE_AND_VERTICALS, resolveDestinationFromDeepLink("/shopping"))
        assertEquals(OpenChatDestination.FINANCE_AND_VERTICALS, resolveDestinationFromDeepLink("/football"))
        assertEquals(OpenChatDestination.LOCKED_CHATS_AND_SAFETY, resolveDestinationFromDeepLink("/parentalcontrols"))
        assertEquals(OpenChatDestination.EXPERIMENT_INSPECTOR, resolveDestinationFromDeepLink("/experiments"))
    }

    @Test
    fun iconCatalogAndMockCollectionsAreComplete() {
        assertEquals(86, OpenChatIcons.AllCatalog.size)
        assertEquals(6, LocalMockRepository.models.size)
        assertEquals(9, LocalMockRepository.codexPets.size)
        assertEquals(7, LocalMockRepository.voicePresets.size)
        assertEquals(14, OpenChatDestination.entries.size)
        assertEquals(0.9f, OpenChatMotion.DEFAULT_SPATIAL_DAMPING, 0.001f)
        assertEquals(700.0f, OpenChatMotion.DEFAULT_SPATIAL_STIFFNESS, 0.01f)
    }

    @Test
    fun temporaryChatAndVoiceCaptionsToggleCleanly() {
        val controller = FakeStateController()
        assertEquals("conv-greeting", controller.state.value.activeConversationId)
        assertEquals(ThemeMode.DARK, controller.state.value.themeMode)
        assertFalse(controller.state.value.isWebSearchEnabled)
        assertFalse(controller.state.value.isTemporaryChat)
        controller.toggleTemporaryChat()
        assertTrue(controller.state.value.isTemporaryChat)

        assertTrue(controller.state.value.isVoiceCaptionsEnabled)
        controller.toggleVoiceCaptions()
        assertFalse(controller.state.value.isVoiceCaptionsEnabled)
    }
}
