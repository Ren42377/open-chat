package io.openchat.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import io.openchat.data.mock.AccentColorOption
import io.openchat.data.mock.ThemeMode

@Composable
fun OpenChatTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    accentColor: AccentColorOption = AccentColorOption.DEFAULT,
    content: @Composable () -> Unit,
) {
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = !isDark
                controller.isAppearanceLightNavigationBars = !isDark
            }
        }
    }

    val resolvedPrimary = when (accentColor) {
        AccentColorOption.DEFAULT -> if (isDark) OpenChatPalette.DarkTextPrimary else OpenChatPalette.LightTextPrimary
        AccentColorOption.BLACK -> if (isDark) OpenChatPalette.DarkTextPrimary else OpenChatPalette.LightTextPrimary
        AccentColorOption.BLUE -> OpenChatPalette.AccentBlue
        AccentColorOption.CYAN -> OpenChatPalette.AccentCyan
        AccentColorOption.GREEN -> OpenChatPalette.AccentGreen
        AccentColorOption.LIME -> OpenChatPalette.AccentLime
        AccentColorOption.MAGENTA -> OpenChatPalette.AccentMagenta
        AccentColorOption.ORANGE -> OpenChatPalette.AccentOrange
        AccentColorOption.PINK -> OpenChatPalette.AccentPink
        AccentColorOption.PURPLE -> OpenChatPalette.AccentPurple
        AccentColorOption.WHITE -> if (isDark) OpenChatPalette.DarkTextPrimary else OpenChatPalette.GrayButton
        AccentColorOption.YELLOW -> OpenChatPalette.AccentYellow
    }

    val onResolvedPrimary = when (accentColor) {
        AccentColorOption.DEFAULT, AccentColorOption.BLACK ->
            if (isDark) OpenChatPalette.DarkBgPrimary else OpenChatPalette.LightBgPrimary
        AccentColorOption.WHITE ->
            if (isDark) OpenChatPalette.DarkBgPrimary else Color.White
        else -> Color.White
    }

    val colorScheme = if (isDark) {
        darkColorScheme(
            primary = resolvedPrimary,
            onPrimary = onResolvedPrimary,
            primaryContainer = OpenChatPalette.DarkBgTertiary,
            onPrimaryContainer = OpenChatPalette.DarkTextPrimary,
            secondary = OpenChatPalette.DarkTextSecondary,
            onSecondary = OpenChatPalette.DarkBgPrimary,
            secondaryContainer = OpenChatPalette.DarkBgTertiary,
            onSecondaryContainer = OpenChatPalette.DarkTextPrimary,
            tertiary = OpenChatPalette.VoiceAccentBlue,
            background = OpenChatPalette.DarkBgPrimary,
            onBackground = OpenChatPalette.DarkTextPrimary,
            surface = OpenChatPalette.DarkBgPrimary,
            onSurface = OpenChatPalette.DarkTextPrimary,
            surfaceVariant = OpenChatPalette.DarkBgTertiary,
            onSurfaceVariant = OpenChatPalette.DarkTextSecondary,
            outline = OpenChatPalette.DarkBorderDefault,
            outlineVariant = OpenChatPalette.DarkBgGroupedElevated,
            error = OpenChatPalette.StatusErrorRed,
            onError = Color.White,
        )
    } else {
        lightColorScheme(
            primary = resolvedPrimary,
            onPrimary = onResolvedPrimary,
            primaryContainer = OpenChatPalette.LightBgTertiary,
            onPrimaryContainer = OpenChatPalette.LightTextPrimary,
            secondary = OpenChatPalette.LightTextSecondary,
            onSecondary = Color.White,
            secondaryContainer = OpenChatPalette.LightBgTertiary,
            onSecondaryContainer = OpenChatPalette.LightTextPrimary,
            tertiary = OpenChatPalette.VoiceAccentBlue,
            background = OpenChatPalette.LightBgPrimary,
            onBackground = OpenChatPalette.LightTextPrimary,
            surface = OpenChatPalette.LightBgPrimary,
            onSurface = OpenChatPalette.LightTextPrimary,
            surfaceVariant = OpenChatPalette.LightBgTertiary,
            onSurfaceVariant = OpenChatPalette.LightTextSecondary,
            outline = OpenChatPalette.LightBorderDefault,
            outlineVariant = OpenChatPalette.LightBorderLight,
            error = OpenChatPalette.StatusErrorRed,
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
