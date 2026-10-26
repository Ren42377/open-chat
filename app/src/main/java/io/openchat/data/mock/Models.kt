package io.openchat.data.mock

enum class UiSimState(val label: String) {
    SUCCESS("Success"),
    LOADING("Loading"),
    EMPTY("Empty"),
    ERROR("Error"),
}

enum class SubscriptionTier(
    val label: String,
    val badge: String,
    val monthlyPrice: String,
    val summary: String,
) {
    FREE("Free plan", "FREE", "$0/mo", "Everyday assistance with standard limits and optional sponsored ads."),
    GO("Go", "GO", "$8/mo", "Faster responses and expanded message limits for everyday conversations."),
    PLUS("Plus", "PLUS", "$20/mo", "Advanced frontier models, ad-free chat, Deep Research, Voice, and image generation."),
    PRO("Pro", "PRO", "$200/mo", "Unlimited frontier intelligence, Codex Cloud, Orbit Dots, and Pro reasoning."),
    PRO_EXTENDED("Pro Extended", "PRO EXT", "$250/mo", "Extended compute allocation for 5.4 and 5.5 models plus priority remote agents."),
    ENTERPRISE("Enterprise", "WORK", "Custom", "Workspace SSO, Lockdown Mode, custom connector rules, and zero model training."),
}

enum class ThemeMode(val label: String) {
    SYSTEM("System default"),
    LIGHT("Use light mode"),
    DARK("Use dark mode"),
}

enum class AccentColorOption(val label: String, val hexPreview: Long) {
    DEFAULT("Default", 0xFF0D0D0D),
    BLACK("Black", 0xFF141218),
    BLUE("Blue", 0xFF2563EB),
    CYAN("Cyan", 0xFF0891B2),
    GREEN("Green", 0xFF16A34A),
    LIME("Lime", 0xFF65A30D),
    MAGENTA("Magenta", 0xFFC026D3),
    ORANGE("Orange", 0xFFEA580C),
    PINK("Pink", 0xFFDB2777),
    PURPLE("Purple", 0xFF4700EB),
    WHITE("White", 0xFFF3EDF7),
    YELLOW("Yellow", 0xFFCA8A04),
}

enum class ReasoningEffort(val label: String, val badge: String) {
    LOW("Low", "Light"),
    MEDIUM("Medium", "Standard"),
    HIGH("High", "High"),
    EXTRA_HIGH("Extra High", "Extra High"),
    MAX("Max", "Max"),
    ULTRA("Ultra", "Ultra"),
}

enum class ConversationMode(val label: String) {
    DEFAULT("Chat"),
    VOICE("Voice"),
    SKETCH("Sketch"),
    WHISPER("Dictation"),
    PROJECT_HOME("Project Home"),
    IMAGES_HOME("Images Home"),
    SHOPPING_HOME("Shopping Home"),
    GIZMO("Custom GPT"),
    TEMPORARY("Temporary Chat"),
}

data class FeatureFlag(
    val key: String,
    val title: String,
    val category: String,
    val description: String,
    val sourceRef: String,
    val enabled: Boolean = true,
)

data class ModelOption(
    val slug: String,
    val name: String,
    val subtitle: String,
    val badge: String,
    val supportsEffort: Boolean,
    val requiresPlusOrHigher: Boolean = false,
    val isInternal: Boolean = false,
)

data class CodeSnippet(
    val language: String,
    val filename: String,
    val code: String,
    val output: String? = null,
)

data class CitationSource(
    val title: String,
    val domain: String,
    val snippet: String,
)

data class ToolExecutionStep(
    val toolName: String,
    val status: String,
    val durationMs: Int,
    val summary: String,
)

data class MessageVariant(
    val optionNumber: Int,
    val label: String,
    val content: String,
    val thinkingDurationSeconds: Int,
)

data class ChatMessage(
    val id: String,
    val isUser: Boolean,
    val senderName: String,
    val timestamp: String,
    val text: String,
    val modelSlug: String = "gpt-5.4",
    val thinkingSeconds: Int = 0,
    val reasoningSteps: List<String> = emptyList(),
    val codeSnippets: List<CodeSnippet> = emptyList(),
    val citations: List<CitationSource> = emptyList(),
    val toolSteps: List<ToolExecutionStep> = emptyList(),
    val variants: List<MessageVariant> = emptyList(),
    val selectedVariantIndex: Int = 0,
    val imageAttachmentTitle: String? = null,
    val reaction: String? = null,
)

data class ConversationThread(
    val id: String,
    val title: String,
    val category: String,
    val updatedRelative: String,
    val modelSlug: String,
    val mode: ConversationMode = ConversationMode.DEFAULT,
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val isLocked: Boolean = false,
    val isTemporary: Boolean = false,
    val isWorkMode: Boolean = false,
    val spaceId: String? = null,
    val gizmoId: String? = null,
    val branchedFromTitle: String? = null,
    val messages: List<ChatMessage> = emptyList(),
)

enum class ArtifactCategory(val label: String) {
    ALL("All"),
    IMAGES("Images"),
    FILES("Files"),
    FOLDERS("Folders"),
    PAGES("Pages"),
    SITES("Sites"),
    FAVORITES("Favorites"),
    SHARED("Shared"),
    RECENTLY_DELETED("Recently deleted"),
}

data class ArtifactItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: ArtifactCategory,
    val updatedRelative: String,
    val sizeLabel: String,
    val isFavorite: Boolean = false,
    val isShared: Boolean = false,
    val sharedBy: String? = null,
    val previewContent: String,
    val siteTrafficViews: Int = 0,
    val siteUniqueVisitors: Int = 0,
    val envVars: Map<String, String> = emptyMap(),
)

data class GizmoItem(
    val id: String,
    val name: String,
    val author: String,
    val category: String,
    val rating: String,
    val conversationsCount: String,
    val description: String,
    val capabilities: List<String>,
    val starterPrompts: List<String>,
    val isPinnedInDrawer: Boolean = false,
)

data class PluginConnectorItem(
    val id: String,
    val name: String,
    val developer: String,
    val category: String,
    val isConnected: Boolean,
    val accountEmail: String?,
    val permissionMode: String,
    val proactiveEnabled: Boolean,
    val skills: List<String>,
    val description: String,
)

data class ProjectSpaceItem(
    val id: String,
    val name: String,
    val description: String,
    val updatedRelative: String,
    val customInstructions: String,
    val collaborators: List<String>,
    val sourceFiles: List<String>,
    val conversationCount: Int,
)

data class ScheduledTaskItem(
    val id: String,
    val title: String,
    val cadenceBadge: String,
    val scheduleSummary: String,
    val promptInstruction: String,
    val lastRunSummary: String,
    val status: String,
    val notifyWhenComplete: Boolean = true,
)

data class MemoryEntry(
    val id: String,
    val content: String,
    val sourceChatTitle: String,
    val createdRelative: String,
    val isFinancial: Boolean = false,
)

data class VoicePreset(
    val id: String,
    val name: String,
    val tagline: String,
    val toneDescription: String,
)

data class CodexRemoteSession(
    val id: String,
    val title: String,
    val hostName: String,
    val hostType: String,
    val directory: String,
    val branchWorktree: String,
    val status: String,
    val modelSlug: String,
    val effort: ReasoningEffort,
    val contextPercentLeft: Int,
    val contextUsedSummary: String,
    val rateLimitReset: String,
    val permissionMode: String,
    val pendingCommandApproval: String? = null,
    val fileChanges: List<String> = emptyList(),
    val terminalOutput: List<String> = emptyList(),
)

data class CodexCompanionPet(
    val id: String,
    val name: String,
    val tagline: String,
    val asciiPreview: String,
    val isSelected: Boolean = false,
)

data class OrbitDotAgent(
    val id: String,
    val name: String,
    val status: String,
    val workingDuration: String,
    val avatarColor: String,
    val avatarShape: String,
    val avatarEyes: String,
    val avatarGlasses: String,
    val emailAddress: String,
    val phoneNumber: String,
    val connectedApps: List<String>,
    val customRules: List<String>,
    val ideasFeed: List<String>,
)

data class CalpicoThread(
    val id: String,
    val title: String,
    val participants: List<String>,
    val lastMessage: String,
    val updatedRelative: String,
    val unreadCount: Int = 0,
    val isRequest: Boolean = false,
    val assistantRespondMode: String = "When mentioned",
)

data class FinanceAccount(
    val id: String,
    val institution: String,
    val accountName: String,
    val category: String,
    val balanceFormatted: String,
    val sharePercentage: Int,
    val status: String = "Updated recently",
)

data class WalletCard(
    val id: String,
    val brand: String,
    val last4: String,
    val expiry: String,
    val cardholder: String,
    val isDefault: Boolean = false,
)

data class ShoppingProduct(
    val id: String,
    val title: String,
    val merchant: String,
    val price: String,
    val taxAndShipping: String,
    val rating: String,
    val whyYouMightLike: String,
    val reviewHighlights: String,
)

data class SportsMatch(
    val id: String,
    val competition: String,
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: Int,
    val awayScore: Int,
    val status: String,
    val summary: String,
)

data class FamilyMemberRule(
    val id: String,
    val name: String,
    val role: String,
    val status: String,
    val quietHours: String,
    val studyHours: String,
    val reduceSensitiveContent: Boolean,
)

data class TrustedContactItem(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val status: String,
)

data class SecuritySession(
    val id: String,
    val deviceName: String,
    val locationAndTime: String,
    val isCurrentSession: Boolean,
    val isTrustedDevice: Boolean,
)
