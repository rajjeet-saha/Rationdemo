package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
  primary = CivicGreenPrimary,
  onPrimary = Color.White,
  primaryContainer = CivicGreenContainer,
  onPrimaryContainer = OnCivicGreenContainer,
  secondary = CivicGreenLight,
  onSecondary = Color.White,
  tertiary = CivicAmber,
  onTertiary = Color.White,
  tertiaryContainer = CivicAmberContainer,
  onTertiaryContainer = OnCivicAmberContainer,
  background = CivicBackgroundLight,
  onBackground = CivicTextDark,
  surface = CivicSurfaceLight,
  onSurface = CivicTextDark,
  surfaceVariant = CivicSurfaceVariantLight,
  onSurfaceVariant = CivicTextMuted,
  outline = CivicOutline,
  error = CivicRedRisk,
  onError = Color.White,
  errorContainer = CivicRedContainer,
  onErrorContainer = OnCivicRedContainer
)

private val DarkColorScheme = darkColorScheme(
  primary = Color(0xFF86D89B),
  onPrimary = CivicGreenDark,
  primaryContainer = CivicGreenLight,
  onPrimaryContainer = Color(0xFFD4EAD8),
  secondary = Color(0xFFA5D6A7),
  onSecondary = Color(0xFF0F3819),
  tertiary = Color(0xFFFBBF24),
  onTertiary = Color(0xFF451A03),
  background = Color(0xFF111713),
  onBackground = Color(0xFFE2E7E3),
  surface = Color(0xFF18201B),
  onSurface = Color(0xFFE2E7E3),
  surfaceVariant = Color(0xFF28342C),
  onSurfaceVariant = Color(0xFFB5C2B8),
  outline = Color(0xFF4A5C50),
  error = Color(0xFFF87171),
  onError = Color(0xFF450A0A)
)

@Composable
fun RationSaathiTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use intentional civic trust branding
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  RationSaathiTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
