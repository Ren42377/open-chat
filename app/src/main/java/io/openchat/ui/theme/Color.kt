package io.openchat.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Color tokens extracted from `_decompiled_ref/res/values/colors.xml`,
 * `_decompiled_ref/res/values-night/colors.xml`, and theme options in `strings.xml`.
 */
object OpenChatPalette {
    // Core Light & Dark surfaces (`res/values/colors.xml` & `values-night/colors.xml`)
    val LightBackground = Color(0xFFFFFFFF)
    val LightPrimary = Color(0xFF0D0D0D)
    val DarkBackground = Color(0xFF000000)
    val DarkPrimary = Color(0xFFFFFFFF)

    // Brand & Accent tokens (`colorPrimary`, `colorPrimaryVariant`, `colorSecondary`)
    val BrandPurple = Color(0xFF4700EB)
    val BrandPurpleVariant = Color(0xFF3000BB)
    val BrandSecondary = Color(0xFFAA85FF)
    val GrayButton = Color(0xFF3C3E49)
    val Pi2DefaultBackground = Color(0xFF21222D)
    val SplashIconShine = Color(0xFF424242)

    // Call & Semantic status colors (`call_notification_answer_color`, `call_notification_decline_color`)
    val CallAnswerGreen = Color(0xFF1D873B)
    val CallDeclineRed = Color(0xFFD93025)
    val WarningAmber = Color(0xFFE37400)
    val InfoBlue = Color(0xFF1A73E8)

    // Material 3 neutral reference palette from `_decompiled_ref/res/values/colors.xml`
    val Neutral0 = Color(0xFF000000)
    val Neutral4 = Color(0xFF0F0D13)
    val Neutral6 = Color(0xFF141218)
    val Neutral10 = Color(0xFF1D1B20)
    val Neutral12 = Color(0xFF211F26)
    val Neutral17 = Color(0xFF2B2930)
    val Neutral20 = Color(0xFF322F35)
    val Neutral22 = Color(0xFF36343B)
    val Neutral24 = Color(0xFF3B383E)
    val Neutral87 = Color(0xFFDED8E1)
    val Neutral90 = Color(0xFFE6E0E9)
    val Neutral92 = Color(0xFFECE6F0)
    val Neutral94 = Color(0xFFF3EDF7)
    val Neutral95 = Color(0xFFF5EFF7)
    val Neutral96 = Color(0xFFF7F2FA)
    val Neutral98 = Color(0xFFFEF7FF)
    val Neutral100 = Color(0xFFFFFFFF)

    // Personalization theme accent colors (`strings.xml` lines 24-36)
    val AccentBlue = Color(0xFF2563EB)
    val AccentCyan = Color(0xFF0891B2)
    val AccentGreen = Color(0xFF16A34A)
    val AccentLime = Color(0xFF65A30D)
    val AccentMagenta = Color(0xFFC026D3)
    val AccentOrange = Color(0xFFEA580C)
    val AccentPink = Color(0xFFDB2777)
    val AccentPurple = Color(0xFF7C3AED)
    val AccentYellow = Color(0xFFCA8A04)
}
