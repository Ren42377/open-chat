package io.openchat.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import io.openchat.data.mock.AccentColorOption
import io.openchat.data.mock.ThemeMode

@Composable
fun OpenChatTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    accentColor: AccentColorOption = AccentColorOption.DEFAULT,
    content: @Composable () -> Unit,
) {
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val resolvedPrimary = when (accentColor) {
        AccentColorOption.DEFAULT -> if (isDark) OpenChatPalette.DarkPrimary else OpenChatPalette.LightPrimary
        AccentColorOption.BLACK -> if (isDark) OpenChatPalette.Neutral90 else OpenChatPalette.LightPrimary
        AccentColorOption.BLUE -> OpenChatPalette.AccentBlue
        AccentColorOption.CYAN -> OpenChatPalette.AccentCyan
        AccentColorOption.GREEN -> OpenChatPalette.AccentGreen
        AccentColorOption.LIME -> OpenChatPalette.AccentLime
        AccentColorOption.MAGENTA -> OpenChatPalette.AccentMagenta
        AccentColorOption.ORANGE -> OpenChatPalette.AccentOrange
        AccentColorOption.PINK -> OpenChatPalette.AccentPink
        AccentColorOption.PURPLE -> OpenChatPalette.BrandPurple
        AccentColorOption.WHITE -> if (isDark) OpenChatPalette.DarkPrimary else OpenChatPalette.GrayButton
        AccentColorOption.YELLOW -> OpenChatPalette.AccentYellow
    }

    val onResolvedPrimary = when (accentColor) {
        AccentColorOption.DEFAULT -> if (isDark) OpenChatPalette.DarkBackground else OpenChatPalette.LightBackground
        AccentColorOption.WHITE -> if (isDark) OpenChatPalette.DarkBackground else Color.White
        else -> Color.White
    }

    val colorScheme = if (isDark) {
        darkColorScheme(
            primary = resolvedPrimary,
            onPrimary = onResolvedPrimary,
            primaryContainer = OpenChatPalette.Neutral20,
            onPrimaryContainer = OpenChatPalette.Neutral95,
            secondary = OpenChatPalette.BrandSecondary,
            onSecondary = OpenChatPalette.Neutral10,
            secondaryContainer = OpenChatPalette.Neutral22,
            onSecondaryContainer = OpenChatPalette.Neutral92,
            tertiary = OpenChatPalette.CallAnswerGreen,
            background = OpenChatPalette.DarkBackground,
            onBackground = OpenChatPalette.DarkPrimary,
            surface = OpenChatPalette.Neutral6,
            onSurface = OpenChatPalette.DarkPrimary,
            surfaceVariant = OpenChatPalette.Neutral17,
            onSurfaceVariant = OpenChatPalette.Neutral87,
            outline = OpenChatPalette.Neutral24,
            error = OpenChatPalette.CallDeclineRed,
            onError = Color.White,
        )
    } else {
        lightColorScheme(
            primary = resolvedPrimary,
            onPrimary = onResolvedPrimary,
            primaryContainer = OpenChatPalette.Neutral94,
            onPrimaryContainer = OpenChatPalette.LightPrimary,
            secondary = OpenChatPalette.BrandPurple,
            onSecondary = Color.White,
            secondaryContainer = OpenChatPalette.Neutral92,
            onSecondaryContainer = OpenChatPalette.Neutral12,
            tertiary = OpenChatPalette.CallAnswerGreen,
            background = OpenChatPalette.LightBackground,
            onBackground = OpenChatPalette.LightPrimary,
            surface = OpenChatPalette.LightBackground,
            onSurface = OpenChatPalette.LightPrimary,
            surfaceVariant = OpenChatPalette.Neutral95,
            onSurfaceVariant = OpenChatPalette.GrayButton,
            outline = OpenChatPalette.Neutral87,
            error = OpenChatPalette.CallDeclineRed,
            onError = Color.White,
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = OpenChatTypography,
        shapes = OpenChatShapes,
        content = content,
    )
}
