package io.openchat.data.mock

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AppSnapshot(
    val uiSimState: UiSimState = UiSimState.SUCCESS,
    val subscriptionTier: SubscriptionTier = SubscriptionTier.PRO,
    val themeMode: ThemeMode = ThemeMode.DARK,
    val accentColor: AccentColorOption = AccentColorOption.DEFAULT,
    val featureFlags: List<FeatureFlag> = LocalMockRepository.defaultFeatureFlags,
    val models: List<ModelOption> = LocalMockRepository.models,
    val selectedModelSlug: String = "gpt-5.4",
    val selectedEffort: ReasoningEffort = ReasoningEffort.HIGH,
    val conversations: List<ConversationThread> = LocalMockRepository.initialConversations,
    val activeConversationId: String = "conv-greeting",
    val activeConversationMode: ConversationMode = ConversationMode.DEFAULT,
    val isTemporaryChat: Boolean = false,
    val isWorkMode: Boolean = false,
    val isWebSearchEnabled: Boolean = false,
    val isDeepResearchEnabled: Boolean = false,
    val isLockdownModeEnabled: Boolean = false,
    val isDaybreakEnabled: Boolean = false,
    val isClinicianModeEnabled: Boolean = false,
    val isMemoryEnabled: Boolean = true,
    val isImproveModelEnabled: Boolean = true,
    val isPersonalizedAdsEnabled: Boolean = false,
    val isBackgroundVoiceEnabled: Boolean = true,
    val selectedVoiceId: String = "sol",
    val isVoiceMuted: Boolean = false,
    val isVoiceCameraLive: Boolean = false,
    val isVoiceCaptionsEnabled: Boolean = true,
    val showVoiceDebugOverlay: Boolean = false,
    val isLockedChatsUnlocked: Boolean = false,
    val lockedChatsPin: String = "2468",
    val artifacts: List<ArtifactItem> = LocalMockRepository.artifacts,
    val selectedArtifactCategory: ArtifactCategory = ArtifactCategory.ALL,
    val gizmos: List<GizmoItem> = LocalMockRepository.gizmos,
    val connectors: List<PluginConnectorItem> = LocalMockRepository.connectors,
    val projectSpaces: List<ProjectSpaceItem> = LocalMockRepository.projectSpaces,
    val scheduledTasks: List<ScheduledTaskItem> = LocalMockRepository.scheduledTasks,
    val memories: List<MemoryEntry> = LocalMockRepository.memories,
    val customDictionary: List<String> = LocalMockRepository.customDictionaryWords,
    val voicePresets: List<VoicePreset> = LocalMockRepository.voicePresets,
    val codexSessions: List<CodexRemoteSession> = LocalMockRepository.codexSessions,
    val codexPets: List<CodexCompanionPet> = LocalMockRepository.codexPets,
    val selectedPetId: String = "codex",
    val orbitDots: List<OrbitDotAgent> = LocalMockRepository.orbitDots,
    val calpicoThreads: List<CalpicoThread> = LocalMockRepository.calpicoThreads,
    val financeAccounts: List<FinanceAccount> = LocalMockRepository.financeAccounts,
    val isFinanceBalanceHidden: Boolean = false,
    val walletCards: List<WalletCard> = LocalMockRepository.walletCards,
    val shoppingProducts: List<ShoppingProduct> = LocalMockRepository.shoppingProducts,
    val sportsMatches: List<SportsMatch> = LocalMockRepository.sportsMatches,
    val familyMembers: List<FamilyMemberRule> = LocalMockRepository.familyMembers,
    val trustedContacts: List<TrustedContactItem> = LocalMockRepository.trustedContacts,
    val securitySessions: List<SecuritySession> = LocalMockRepository.securitySessions,
    val userFullName: String = "Alex Rivera",
    val userUsername: String = "alexrivera",
    val userEmail: String = "alex.rivera@openchat.io",
    val userOccupation: String = "Principal Android & Systems Architect",
    val customInstructionsTraits: String = "Concise, rigorous, code-first, zero emojis, zero em dashes.",
    val userFunFacts: List<String> = listOf(
        "Builds clean-room Compose interfaces from decompiled specs",
        "Collects custom split mechanical keyboards",
        "Runs early-morning trail ultramarathons",
    ),
    val lastStatusBanner: String? = null,
) {
    val activeConversation: ConversationThread?
        get() = conversations.find { it.id == activeConversationId } ?: conversations.firstOrNull()

    fun isFlagEnabled(key: String): Boolean =
        featureFlags.find { it.key == key }?.enabled ?: true
}

class FakeStateController {
    private val _state = MutableStateFlow(AppSnapshot())
    val state: StateFlow<AppSnapshot> = _state.asStateFlow()

    fun setUiSimState(target: UiSimState) {
        _state.update { it.copy(uiSimState = target) }
    }

    fun setSubscriptionTier(tier: SubscriptionTier) {
        _state.update {
            it.copy(
                subscriptionTier = tier,
                lastStatusBanner = "Switched plan to ${tier.label}",
            )
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        _state.update { it.copy(themeMode = mode) }
    }

    fun setAccentColor(color: AccentColorOption) {
        _state.update { it.copy(accentColor = color) }
    }

    fun toggleFeatureFlag(key: String) {
        _state.update { current ->
            val updated = current.featureFlags.map { flag ->
                if (flag.key == key) flag.copy(enabled = !flag.enabled) else flag
            }
            current.copy(featureFlags = updated)
        }
    }

    fun setAllFeatureFlags(enabled: Boolean) {
        _state.update { current ->
            current.copy(
                featureFlags = current.featureFlags.map { it.copy(enabled = enabled) },
            )
        }
    }

    fun selectModel(slug: String) {
        _state.update { it.copy(selectedModelSlug = slug) }
    }

    fun selectEffort(effort: ReasoningEffort) {
        _state.update {
            it.copy(
                selectedEffort = effort,
                lastStatusBanner = "Adjust effort. ${effort.label} selected.",
            )
        }
    }

    fun selectConversation(conversationId: String) {
        _state.update { current ->
            val thread = current.conversations.find { it.id == conversationId }
            current.copy(
                activeConversationId = conversationId,
                selectedModelSlug = thread?.modelSlug ?: current.selectedModelSlug,
                activeConversationMode = thread?.mode ?: ConversationMode.DEFAULT,
                isTemporaryChat = thread?.isTemporary ?: false,
                isWorkMode = thread?.isWorkMode ?: false,
            )
        }
    }

    fun startNewChat(
        mode: ConversationMode = ConversationMode.DEFAULT,
        isTemporary: Boolean = false,
        spaceId: String? = null,
        gizmoId: String? = null,
    ) {
        val newId = "conv-${System.currentTimeMillis()}"
        val title = when {
            isTemporary -> "Temporary chat"
            mode == ConversationMode.IMAGES_HOME -> "Create an image"
            mode == ConversationMode.SHOPPING_HOME -> "Shopping research"
            gizmoId != null -> "Custom GPT chat"
            spaceId != null -> "Project conversation"
            else -> "New chat"
        }
        val newThread = ConversationThread(
            id = newId,
            title = title,
            category = "Today",
            updatedRelative = "Just now",
            modelSlug = _state.value.selectedModelSlug,
            mode = mode,
            isTemporary = isTemporary,
            spaceId = spaceId,
            gizmoId = gizmoId,
            messages = emptyList(),
        )
        _state.update { current ->
            current.copy(
                conversations = listOf(newThread) + current.conversations,
                activeConversationId = newId,
                activeConversationMode = mode,
                isTemporaryChat = isTemporary,
            )
        }
    }

    fun sendUserMessage(prompt: String, attachmentTitle: String? = null) {
        val trimmed = prompt.trim()
        if (trimmed.isEmpty() && attachmentTitle == null) return

        _state.update { current ->
            val targetId = current.activeConversationId
            val userMsg = ChatMessage(
                id = "msg-u-${System.currentTimeMillis()}",
                isUser = true,
                senderName = "You",
                timestamp = "Just now",
                text = if (trimmed.isEmpty()) "Attached: $attachmentTitle" else trimmed,
                modelSlug = current.selectedModelSlug,
                imageAttachmentTitle = attachmentTitle,
            )

            val responseText = when {
                trimmed.startsWith("/goal") -> "Goal created and scheduled. I broke your request into verifiable milestones with automated status checks."
                trimmed.startsWith("/plan") -> "Plan mode active. Step 1: Inspect decompiled references. Step 2: Reconstruct tokens and mock state. Step 3: Verify CI green."
                current.isDeepResearchEnabled -> "### Deep Research Synthesis\n\nI cross-checked 14 primary sources for **\"$trimmed\"** using **${current.selectedModelSlug}** (${current.selectedEffort.label} effort). Key takeaways:\n\n1. Verified deterministic state transitions across all 4 UI branches (`Success`, `Loading`, `Empty`, `Error`).\n2. Confirmed spatial spring motion parameters (`0.9` damping, `700.0` stiffness)."
                current.isWebSearchEnabled -> "Searched the web and synthesized current findings for **\"$trimmed\"**:\n\n- Primary documentation confirms compatibility with Android API 36 and Compose BOM 2026.09.00.\n- All local mock repositories remain 100% offline and deterministic."
                else -> "Processed **\"$trimmed\"** with **${current.selectedModelSlug}** (${current.selectedEffort.label} effort). You can inspect reasoning steps, switch variants in stream, or copy code blocks below."
            }

            val assistantMsg = ChatMessage(
                id = "msg-a-${System.currentTimeMillis() + 1}",
                isUser = false,
                senderName = "Open Chat",
                timestamp = "Just now",
                modelSlug = current.selectedModelSlug,
                thinkingSeconds = if (current.selectedEffort == ReasoningEffort.LOW) 2 else 7,
                reasoningSteps = listOf(
                    "Analyzed prompt intent and active workspace context.",
                    "Applied ${current.selectedEffort.label} reasoning effort on ${current.selectedModelSlug}.",
                ),
                text = responseText,
                citations = if (current.isWebSearchEnabled) {
                    listOf(
                        CitationSource(
                            title = "Reference Specification & Release Notes",
                            domain = "docs.openchat.local",
                            snippet = "Verified configuration and UI token metrics.",
                        ),
                    )
                } else {
                    emptyList()
                },
                variants = listOf(
                    MessageVariant(1, "Option 1: Direct synthesis", responseText, 5),
                    MessageVariant(2, "Option 2: Executive bullet summary", "- **Summary:** $responseText\n- **Next Step:** Open In-App Inspector to test edge states.", 7),
                ),
            )

            val updatedThreads = current.conversations.map { thread ->
                if (thread.id == targetId) {
                    val updatedTitle = if (thread.messages.isEmpty() && trimmed.isNotEmpty()) {
                        trimmed.take(42)
                    } else {
                        thread.title
                    }
                    thread.copy(
                        title = updatedTitle,
                        updatedRelative = "Just now",
                        messages = thread.messages + userMsg + assistantMsg,
                    )
                } else {
                    thread
                }
            }

            current.copy(conversations = updatedThreads)
        }
    }

    fun selectMessageVariant(messageId: String, variantIndex: Int) {
        _state.update { current ->
            val updatedThreads = current.conversations.map { thread ->
                if (thread.id == current.activeConversationId) {
                    thread.copy(
                        messages = thread.messages.map { msg ->
                            if (msg.id == messageId && variantIndex in msg.variants.indices) {
                                msg.copy(
                                    selectedVariantIndex = variantIndex,
                                    text = msg.variants[variantIndex].content,
                                )
                            } else {
                                msg
                            }
                        },
                    )
                } else {
                    thread
                }
            }
            current.copy(
                conversations = updatedThreads,
                lastStatusBanner = "I prefer this version (Option ${variantIndex + 1})",
            )
        }
    }

    fun togglePinConversation(conversationId: String) {
        _state.update { current ->
            current.copy(
                conversations = current.conversations.map {
                    if (it.id == conversationId) it.copy(isPinned = !it.isPinned) else it
                },
            )
        }
    }

    fun toggleArchiveConversation(conversationId: String) {
        _state.update { current ->
            current.copy(
                conversations = current.conversations.map {
                    if (it.id == conversationId) it.copy(isArchived = !it.isArchived) else it
                },
                lastStatusBanner = "Chat archive status updated",
            )
        }
    }

    fun toggleLockConversation(conversationId: String) {
        _state.update { current ->
            current.copy(
                conversations = current.conversations.map {
                    if (it.id == conversationId) it.copy(isLocked = !it.isLocked) else it
                },
                lastStatusBanner = "Updated Locked Chats protection",
            )
        }
    }

    fun unlockLockedChatsWithPin(pinInput: String): Boolean {
        val matches = pinInput == _state.value.lockedChatsPin
        if (matches) {
            _state.update {
                it.copy(
                    isLockedChatsUnlocked = true,
                    lastStatusBanner = "Locked chats unlocked",
                )
            }
        }
        return matches
    }

    fun updateLockedChatsPin(newPin: String) {
        if (newPin.length >= 4) {
            _state.update {
                it.copy(
                    lockedChatsPin = newPin,
                    isLockedChatsUnlocked = true,
                    lastStatusBanner = "Your account PIN is ready.",
                )
            }
        }
    }

    fun toggleWebSearch() {
        _state.update { it.copy(isWebSearchEnabled = !it.isWebSearchEnabled) }
    }

    fun toggleDeepResearch() {
        _state.update { it.copy(isDeepResearchEnabled = !it.isDeepResearchEnabled) }
    }

    fun toggleWorkMode() {
        _state.update {
            val next = !it.isWorkMode
            it.copy(
                isWorkMode = next,
                lastStatusBanner = if (next) "Conversation moved to Work." else "Returned to Chat",
            )
        }
    }

    fun toggleTemporaryChat() {
        _state.update {
            val next = !it.isTemporaryChat
            it.copy(
                isTemporaryChat = next,
                activeConversationMode = if (next) ConversationMode.TEMPORARY else ConversationMode.DEFAULT,
            )
        }
    }

    fun deleteConversation(conversationId: String) {
        _state.update { current ->
            val remaining = current.conversations.filterNot { it.id == conversationId }
            val nextActiveId = if (current.activeConversationId == conversationId) {
                remaining.firstOrNull()?.id ?: ""
            } else {
                current.activeConversationId
            }
            current.copy(
                conversations = remaining,
                activeConversationId = nextActiveId,
                lastStatusBanner = "Conversation deleted",
            )
        }
    }

    fun renameConversation(conversationId: String, newTitle: String) {
        val clean = newTitle.trim()
        if (clean.isEmpty()) return
        _state.update { current ->
            current.copy(
                conversations = current.conversations.map {
                    if (it.id == conversationId) it.copy(title = clean) else it
                },
            )
        }
    }

    fun toggleVoiceCaptions() {
        _state.update { it.copy(isVoiceCaptionsEnabled = !it.isVoiceCaptionsEnabled) }
    }

    fun showToastBanner(message: String) {
        _state.update { it.copy(lastStatusBanner = message) }
    }

    fun selectArtifactCategory(category: ArtifactCategory) {
        _state.update { it.copy(selectedArtifactCategory = category) }
    }

    fun toggleArtifactFavorite(artifactId: String) {
        _state.update { current ->
            current.copy(
                artifacts = current.artifacts.map {
                    if (it.id == artifactId) it.copy(isFavorite = !it.isFavorite) else it
                },
            )
        }
    }

    fun createArtifactFolder(folderName: String) {
        val clean = folderName.trim()
        if (clean.isEmpty()) return
        val newFolder = ArtifactItem(
            id = "art-folder-${System.currentTimeMillis()}",
            title = clean,
            subtitle = "Folder | 0 items | Owned by you",
            category = ArtifactCategory.FOLDERS,
            updatedRelative = "Created just now",
            sizeLabel = "0 items",
            previewContent = "This folder is empty. Add files, pages, or images from your library.",
        )
        _state.update { current ->
            current.copy(
                artifacts = listOf(newFolder) + current.artifacts,
                lastStatusBanner = "Created folder \"$clean\"",
            )
        }
    }

    fun restoreDeletedArtifact(artifactId: String) {
        _state.update { current ->
            current.copy(
                artifacts = current.artifacts.map {
                    if (it.id == artifactId) {
                        it.copy(
                            category = ArtifactCategory.FILES,
                            subtitle = "Restored file",
                            updatedRelative = "Restored just now",
                        )
                    } else {
                        it
                    }
                },
                lastStatusBanner = "File restored",
            )
        }
    }

    fun toggleConnector(connectorId: String) {
        _state.update { current ->
            current.copy(
                connectors = current.connectors.map {
                    if (it.id == connectorId) {
                        val next = !it.isConnected
                        it.copy(
                            isConnected = next,
                            accountEmail = if (next) current.userEmail else null,
                        )
                    } else {
                        it
                    }
                },
            )
        }
    }

    fun createProjectSpace(name: String, instructions: String) {
        val cleanName = name.trim()
        if (cleanName.isEmpty()) return
        val newSpace = ProjectSpaceItem(
            id = "space-${System.currentTimeMillis()}",
            name = cleanName,
            description = "Custom project workspace",
            updatedRelative = "Created just now",
            customInstructions = instructions.ifBlank { "Provide structured, well-cited answers." },
            collaborators = listOf("${_state.value.userFullName} (Owner)"),
            sourceFiles = emptyList(),
            conversationCount = 1,
        )
        _state.update { current ->
            current.copy(
                projectSpaces = listOf(newSpace) + current.projectSpaces,
                lastStatusBanner = "Created Space \"$cleanName\"",
            )
        }
    }

    fun createScheduledTask(title: String, cadence: String, prompt: String) {
        val cleanTitle = title.trim()
        if (cleanTitle.isEmpty()) return
        val newTask = ScheduledTaskItem(
            id = "task-${System.currentTimeMillis()}",
            title = cleanTitle,
            cadenceBadge = cadence.uppercase(),
            scheduleSummary = "$cadence | 9:00 AM",
            promptInstruction = prompt.ifBlank { "Summarize daily updates." },
            lastRunSummary = "Not run yet",
            status = "SCHEDULED",
        )
        _state.update { current ->
            current.copy(
                scheduledTasks = listOf(newTask) + current.scheduledTasks,
                lastStatusBanner = "Scheduled task created",
            )
        }
    }

    fun toggleScheduledTaskPause(taskId: String) {
        _state.update { current ->
            current.copy(
                scheduledTasks = current.scheduledTasks.map {
                    if (it.id == taskId) {
                        val nextStatus = if (it.status == "PAUSED") "SCHEDULED" else "PAUSED"
                        it.copy(status = nextStatus)
                    } else {
                        it
                    }
                },
            )
        }
    }

    fun addMemory(content: String) {
        val clean = content.trim()
        if (clean.isEmpty()) return
        val entry = MemoryEntry(
            id = "mem-${System.currentTimeMillis()}",
            content = clean,
            sourceChatTitle = "Manual Memory Update",
            createdRelative = "Just now",
        )
        _state.update { current ->
            current.copy(
                memories = listOf(entry) + current.memories,
                lastStatusBanner = "Memory updated",
            )
        }
    }

    fun deleteMemory(memoryId: String) {
        _state.update { current ->
            current.copy(
                memories = current.memories.filterNot { it.id == memoryId },
                lastStatusBanner = "Saved memory deleted",
            )
        }
    }

    fun addDictionaryWord(word: String) {
        val clean = word.trim()
        if (clean.isEmpty() || _state.value.customDictionary.contains(clean)) return
        _state.update { current ->
            current.copy(
                customDictionary = current.customDictionary + clean,
                lastStatusBanner = "Added \"$clean\" to custom dictionary",
            )
        }
    }

    fun selectVoicePreset(voiceId: String) {
        _state.update { it.copy(selectedVoiceId = voiceId) }
    }

    fun toggleVoiceMute() {
        _state.update { it.copy(isVoiceMuted = !it.isVoiceMuted) }
    }

    fun toggleVoiceCamera() {
        _state.update { it.copy(isVoiceCameraLive = !it.isVoiceCameraLive) }
    }

    fun toggleVoiceDebugOverlay() {
        _state.update { it.copy(showVoiceDebugOverlay = !it.showVoiceDebugOverlay) }
    }

    fun approveCodexCommand(sessionId: String) {
        _state.update { current ->
            current.copy(
                codexSessions = current.codexSessions.map { session ->
                    if (session.id == sessionId) {
                        session.copy(
                            status = "Working",
                            pendingCommandApproval = null,
                            terminalOutput = session.terminalOutput + listOf(
                                "Approved command for this session.",
                                "$ ./gradlew lintDebug testDebugUnitTest assembleDebug",
                                "BUILD SUCCESSFUL in 18s (0 warnings)",
                            ),
                        )
                    } else {
                        session
                    }
                },
                lastStatusBanner = "Approved Codex command execution",
            )
        }
    }

    fun selectCodexPet(petId: String) {
        _state.update { current ->
            current.copy(
                selectedPetId = petId,
                codexPets = current.codexPets.map { it.copy(isSelected = it.id == petId) },
                lastStatusBanner = "Pet updated.",
            )
        }
    }

    fun addOrbitDotRule(ruleText: String) {
        val clean = ruleText.trim()
        if (clean.isEmpty()) return
        _state.update { current ->
            current.copy(
                orbitDots = current.orbitDots.mapIndexed { index, dot ->
                    if (index == 0) dot.copy(customRules = dot.customRules + clean) else dot
                },
                lastStatusBanner = "Custom rule saved",
            )
        }
    }

    fun updateOrbitDotAvatar(color: String, shape: String, eyes: String, glasses: String) {
        _state.update { current ->
            current.copy(
                orbitDots = current.orbitDots.mapIndexed { index, dot ->
                    if (index == 0) {
                        dot.copy(
                            avatarColor = color,
                            avatarShape = shape,
                            avatarEyes = eyes,
                            avatarGlasses = glasses,
                        )
                    } else {
                        dot
                    }
                },
                lastStatusBanner = "Updated Dot avatar",
            )
        }
    }

    fun acceptCalpicoRequest(threadId: String) {
        _state.update { current ->
            current.copy(
                calpicoThreads = current.calpicoThreads.map {
                    if (it.id == threadId) it.copy(isRequest = false, unreadCount = 0) else it
                },
                lastStatusBanner = "Accepted chat request",
            )
        }
    }

    fun toggleFinanceBalanceHidden() {
        _state.update { it.copy(isFinanceBalanceHidden = !it.isFinanceBalanceHidden) }
    }

    fun addWalletCard(brand: String, last4: String, expiry: String) {
        val clean4 = last4.takeLast(4).padStart(4, '0')
        val card = WalletCard(
            id = "card-${System.currentTimeMillis()}",
            brand = brand,
            last4 = clean4,
            expiry = expiry.ifBlank { "12/29" },
            cardholder = _state.value.userFullName,
            isDefault = false,
        )
        _state.update { current ->
            current.copy(
                walletCards = current.walletCards + card,
                lastStatusBanner = "Card ending in $clean4 added",
            )
        }
    }

    fun toggleLockdownMode() {
        _state.update {
            val next = !it.isLockdownModeEnabled
            it.copy(
                isLockdownModeEnabled = next,
                lastStatusBanner = if (next) "Enabled lockdown mode" else "Disabled lockdown mode",
            )
        }
    }

    fun toggleDaybreak() {
        _state.update {
            val next = !it.isDaybreakEnabled
            it.copy(
                isDaybreakEnabled = next,
                lastStatusBanner = if (next) "Daybreak enabled for authorized cybersecurity work" else "Daybreak turned off",
            )
        }
    }

    fun toggleClinicianMode() {
        _state.update {
            val next = !it.isClinicianModeEnabled
            it.copy(
                isClinicianModeEnabled = next,
                lastStatusBanner = if (next) "Switched to Open Chat for Clinicians" else "Clinician mode off",
            )
        }
    }

    fun toggleMemoryEnabled() {
        _state.update { it.copy(isMemoryEnabled = !it.isMemoryEnabled) }
    }

    fun toggleImproveModel() {
        _state.update { it.copy(isImproveModelEnabled = !it.isImproveModelEnabled) }
    }

    fun togglePersonalizedAds() {
        _state.update { it.copy(isPersonalizedAdsEnabled = !it.isPersonalizedAdsEnabled) }
    }

    fun updateUserProfile(fullName: String, username: String, occupation: String, traits: String) {
        _state.update {
            it.copy(
                userFullName = fullName.ifBlank { it.userFullName },
                userUsername = username.ifBlank { it.userUsername },
                userOccupation = occupation.ifBlank { it.userOccupation },
                customInstructionsTraits = traits.ifBlank { it.customInstructionsTraits },
                lastStatusBanner = "Profile and custom instructions saved",
            )
        }
    }

    fun shuffleFunFacts() {
        _state.update {
            it.copy(userFunFacts = it.userFunFacts.shuffled())
        }
    }

    fun clearStatusBanner() {
        _state.update { it.copy(lastStatusBanner = null) }
    }
}
