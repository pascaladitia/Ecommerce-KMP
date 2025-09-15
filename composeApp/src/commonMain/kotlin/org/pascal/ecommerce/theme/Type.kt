package org.pascal.ecommerce.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ecommerce_kmp.composeapp.generated.resources.Res
import ecommerce_kmp.composeapp.generated.resources.inter
import ecommerce_kmp.composeapp.generated.resources.inter_bold
import ecommerce_kmp.composeapp.generated.resources.inter_extra_bold
import org.jetbrains.compose.resources.Font

// Set of Material typography styles to start with
@Composable
fun getTypography(): Typography {
    return Typography(
        headlineLarge = TextStyle(
            fontFamily = FontFamily(Font(Res.font.inter_extra_bold)),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        headlineMedium = TextStyle(
            fontFamily = FontFamily(Font(Res.font.inter_extra_bold)),
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        headlineSmall = TextStyle(
            fontFamily = FontFamily(Font(Res.font.inter_bold)),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        titleLarge = TextStyle(
            fontFamily = FontFamily(Font(Res.font.inter)),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        titleMedium = TextStyle(
            fontFamily = FontFamily(Font(Res.font.inter)),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        titleSmall = TextStyle(
            fontFamily = FontFamily(Font(Res.font.inter)),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        bodyLarge = TextStyle(
            fontFamily = FontFamily(Font(Res.font.inter)),
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        bodyMedium = TextStyle(
            fontFamily = FontFamily(Font(Res.font.inter)),
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        bodySmall = TextStyle(
            fontFamily = FontFamily(Font(Res.font.inter)),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        labelLarge = TextStyle(
            fontFamily = FontFamily(Font(Res.font.inter)),
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        labelMedium = TextStyle(
            fontFamily = FontFamily(Font(Res.font.inter)),
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        labelSmall = TextStyle(
            fontFamily = FontFamily(Font(Res.font.inter)),
            fontWeight = FontWeight.Normal,
            fontSize = 8.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    )
}