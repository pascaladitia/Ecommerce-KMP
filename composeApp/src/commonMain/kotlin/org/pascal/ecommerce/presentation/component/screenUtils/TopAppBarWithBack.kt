package org.pascal.ecommerce.presentation.component.screenUtils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Bookmark

@Composable
fun TopAppBarWithBack(
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onFavorite: () -> Unit,
    onBackClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth()
            .padding(30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Card(
            modifier = Modifier.width(50.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors =  CardDefaults.cardColors(Color.White)
        ) {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = FeatherIcons.ArrowLeft,
                    contentDescription = ""
                )
            }

        }

        Card(
            modifier = Modifier.width(50.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors =  CardDefaults.cardColors(Color.White)
        ) {
            IconButton(onClick = { onFavorite() }) {
                Icon(
                    imageVector = if (isFavorite) FeatherIcons.Bookmark
                    else FeatherIcons.Bookmark,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

        }
    }
}