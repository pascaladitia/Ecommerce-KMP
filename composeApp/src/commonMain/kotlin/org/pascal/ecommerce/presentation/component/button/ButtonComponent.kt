package org.pascal.ecommerce.presentation.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Home
import ecommerce_kmp.composeapp.generated.resources.Res
import ecommerce_kmp.composeapp.generated.resources.ic_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun ButtonComponent(
    modifier: Modifier = Modifier,
    text: String,
    isIcon: Int = 0,
    icon: ImageVector = FeatherIcons.Home,
    color: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = Color.White,
    onClick: () -> Unit
) {
    ElevatedButton(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(color),
        onClick = { onClick() },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isIcon == 1) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(Res.drawable.ic_logo),
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = textColor,
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (isIcon == 2) {
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun ButtonOutlineComponent(
    modifier: Modifier = Modifier,
    text: String,
    isIcon: Int = 0,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    OutlinedIconButton(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
        border = BorderStroke(1.dp, color),
        shape = RoundedCornerShape(8.dp),
        onClick = { onClick() },
    ) {
        Row {
            if (isIcon == 1) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(Res.drawable.ic_logo),
                    contentDescription = null,
                    tint = color
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = color,
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (isIcon == 2) {
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(Res.drawable.ic_logo),
                    contentDescription = null,
                    tint = color
                )
            }
        }
    }
}