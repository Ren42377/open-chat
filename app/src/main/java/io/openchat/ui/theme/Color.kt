package io.openchat.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Exact OAI color tokens extracted from `_decompiled_ref/assets_pkg/valdi_strings/oai_core.txt`
 * (`OAI_LIGHT_COLOR_PALETTE` and `OAI_DARK_COLOR_PALETTE`) and `res/values/colors.xml`.
 */
object OpenChatPalette {
    // OAI Light Palette (`oai_core/src/OAITheme.js` lines 258-319)
    val LightBgCanvas = Color(0xFFFFFFFF)
    val LightBgCanvasSubtle = Color(0xFFFCFCFC)
    val LightBgPrimary = Color(0xFFFFFFFF)
    val LightBgSecondary = Color(0xFFF9F9F9)
    val LightBgTertiary = Color(0xFFF3F3F3)
    val LightBgLayer3 = Color(0xFFE8E8E8)
    val LightBgUserMessage = Color(0xFFF3F3F3)
    val LightBgCodeBlock = Color(0xFFF8F8F8)
    val LightBgGrouped = Color(0xFFF2F2F7)
    val LightBorderDefault = Color(0xFFEBEBEB)
    val LightBorderLight = Color(0xFFF0F0F0)
    val LightTextPrimary = Color(0xFF0D0D0D)
    val LightTextSecondary = Color(0xFF5D5D5D)
    val LightTextTertiary = Color(0xFF8F8F8F)
    val LightIconPrimary = Color(0xFF0D0D0D)
    val LightIconSecondary = Color(0xFF5D5D5D)
    val LightIconTertiary = Color(0xFF8F8F8F)

    // OAI Dark Palette (`oai_core/src/OAITheme.js` lines 321-355)
    val DarkBgCanvas = Color(0xFF212121)
    val DarkBgDrawer = Color(0xFF181818)
    val DarkBgPrimary = Color(0xFF212121)
    val DarkBgSecondary = Color(0xFF181818)
    val DarkBgTertiary = Color(0xFF303030)
    val DarkBgLayer3 = Color(0xFF414141)
    val DarkBgUserMessage = Color(0xFF303030)
    val DarkBgCodeBlock = Color(0xFF181818)
    val DarkBgGrouped = Color(0xFF1C1C1E)
    val DarkBgGroupedElevated = Color(0xFF2C2C2E)
    val DarkBorderDefault = Color(0xFF38383B)
    val DarkTextPrimary = Color(0xFFFFFFFF)
    val DarkTextSecondary = Color(0xFFAFAFAF)
    val DarkTextTertiary = Color(0xFF8F8F8F)
    val DarkIconPrimary = Color(0xFFFFFFFF)
    val DarkIconSecondary = Color(0xFF9E9E9E)
    val DarkIconTertiary = Color(0xFF8F8F8F)

    // Backward-compatible aliases referenced in screens
    val LightBackground = LightBgPrimary
    val LightPrimary = LightTextPrimary
    val DarkBackground = DarkBgPrimary
    val DarkPrimary = DarkTextPrimary

    // OAI Semantic & Theme Accent Tokens (`oai_core/src/OAITheme.js` lines 288-317)
    val VoiceAccentBlue = Color(0xFF0285FF)
    val VoiceSkyLight = Color(0xFF7AB7FF)
    val VoiceDeepBlue = Color(0xFF0169CC)
    val PlusPillBgLight = Color(0xFFEFE5FE)
    val PlusPillTextLight = Color(0xFF8046D9)
    val PlusPillBgDark = Color(0xFF3F226A)
    val PlusPillTextDark = Color(0xFFAD7BF9)

    val StatusSuccessGreen = Color(0xFF34C759)
    val StatusSuccessTextLight = Color(0xFF00A240)
    val StatusSuccessBgLight = Color(0xFFD9F4E4)
    val StatusWarningOrange = Color(0xFFE25507)
    val StatusWarningBgLight = Color(0xFFFFF5F0)
    val StatusErrorRed = Color(0xFFFA423E)
    val StatusErrorBgLight = Color(0xFFFFF0F0)

    val CallAnswerGreen = Color(0xFF04B84C)
    val CallDeclineRed = Color(0xFFFA423E)
    val WarningAmber = Color(0xFFE25507)
    val InfoBlue = Color(0xFF0285FF)
    val BrandPurple = Color(0xFF8046D9)
    val BrandPurpleVariant = Color(0xFF3F226A)
    val BrandSecondary = Color(0xFFAD7BF9)
    val GrayButton = Color(0xFF5D5D5D)

    // Personalization theme accent colors (`oai_core/src/OAITheme.js` & `strings.xml`)
    val AccentBlue = Color(0xFF0285FF)
    val AccentCyan = Color(0xFF027C6A)
    val AccentGreen = Color(0xFF04B84C)
    val AccentLime = Color(0xFF30D158)
    val AccentMagenta = Color(0xFFC026D3)
    val AccentOrange = Color(0xFFFB6A22)
    val AccentPink = Color(0xFFDB2777)
    val AccentPurple = Color(0xFF8046D9)
    val AccentYellow = Color(0xFFFFC300)
}
