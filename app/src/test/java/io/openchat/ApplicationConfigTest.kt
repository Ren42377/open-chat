package io.openchat

import io.openchat.data.mock.AccentColorOption
import io.openchat.data.mock.ConversationMode
import io.openchat.data.mock.FakeStateController
import io.openchat.data.mock.LocalMockRepository
import io.openchat.data.mock.ReasoningEffort
import io.openchat.data.mock.SubscriptionTier
import io.openchat.data.mock.ThemeMode
import io.openchat.data.mock.UiSimState
import io.openchat.navigation.OpenChatDestination
import io.openchat.ui.theme.OpenChatIcons
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
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
        val initialFlags = controller.snapshot.value.featureFlags
        assertEquals(24, initialFlags.size)
        assertTrue(initialFlags.all { it.enabled })

        val targetFlagKey = initialFlags.first().key
        controller.toggleFeatureFlag(targetFlagKey)
        assertFalse(controller.isFlagEnabled(targetFlagKey))

        controller.enableAllFeatureFlags()
        assertTrue(controller.snapshot.value.featureFlags.all { it.enabled })
    }

    @Test
    fun uiSimStateAndSubscriptionTierTransitionsWorkDeterministically() {
        val controller = FakeStateController()
        assertEquals(UiSimState.SUCCESS, controller.snapshot.value.uiSimState)

        for (simState in UiSimState.entries) {
            controller.setUiSimState(simState)
            assertEquals(simState, controller.snapshot.value.uiSimState)
        }

        for (tier in SubscriptionTier.entries) {
            controller.setSubscriptionTier(tier)
            assertEquals(tier, controller.snapshot.value.subscriptionTier)
        }

        for (theme in ThemeMode.entries) {
            controller.setThemeMode(theme)
            assertEquals(theme, controller.snapshot.value.themeMode)
        }

        for (accent in AccentColorOption.entries) {
            controller.setAccentColor(accent)
            assertEquals(accent, controller.snapshot.value.accentColor)
        }
    }

    @Test
    fun conversationMessagingAndVariantSelectionMutateState() {
        val controller = FakeStateController()
        controller.setConversationMode(ConversationMode.DEEP_RESEARCH)
        controller.setReasoningEffort(ReasoningEffort.HIGH)

        val beforeCount = controller.snapshot.value.conversations
            .first { it.id == controller.snapshot.value.selectedConversationId }
            .messages.size

        controller.sendPrompt("Verify clean-room architecture and offline mock repository")

        val activeThread = controller.snapshot.value.conversations
            .first { it.id == controller.snapshot.value.selectedConversationId }
        assertEquals(beforeCount + 2, activeThread.messages.size)

        controller.selectMessageVariant(
            conversationId = "conv-architecture",
            messageId = "msg-arch-2",
            variantIndex = 1,
        )
        val updatedMessage = controller.snapshot.value.conversations
            .first { it.id == "conv-architecture" }
            .messages.first { it.id == "msg-arch-2" }
        assertEquals(1, updatedMessage.selectedVariantIndex)
    }

    @Test
    fun lockedChatsPinAndCodexRemoteActionsUpdateSnapshot() {
        val controller = FakeStateController()
        assertFalse(controller.snapshot.value.lockedChatsUnlocked)

        val wrongAttempt = controller.unlockLockedChatsWithPin("0000")
        assertFalse(wrongAttempt)
        assertFalse(controller.snapshot.value.lockedChatsUnlocked)

        val validAttempt = controller.unlockLockedChatsWithPin("2468")
        assertTrue(validAttempt)
        assertTrue(controller.snapshot.value.lockedChatsUnlocked)

        controller.relockChats()
        assertFalse(controller.snapshot.value.lockedChatsUnlocked)

        val sessionWithApproval = controller.snapshot.value.codexSessions.first { it.requiresApproval }
        controller.approveCodexCommand(sessionWithApproval.id)
        val updatedSession = controller.snapshot.value.codexSessions.first { it.id == sessionWithApproval.id }
        assertFalse(updatedSession.requiresApproval)

        controller.sendCodexRemoteCommand("/compact")
        assertNotNull(controller.snapshot.value.statusBannerMessage)
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
        val icons = OpenChatIcons.allIcons()
        assertEquals(48, icons.size)

        val snapshot = LocalMockRepository.createDefaultSnapshot()
        assertEquals(6, snapshot.models.size)
        assertEquals(9, snapshot.codexPets.size)
        assertEquals(7, snapshot.voicePresets.size)
        assertEquals(14, OpenChatDestination.entries.size)
    }
}
