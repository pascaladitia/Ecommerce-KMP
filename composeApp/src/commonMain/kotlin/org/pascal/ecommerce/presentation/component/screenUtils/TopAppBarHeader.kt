package org.pascal.ecommerce.presentation.component.screenUtils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import compose.icons.FeatherIcons
import compose.icons.feathericons.Menu
import ecommerce_kmp.composeapp.generated.resources.Res
import ecommerce_kmp.composeapp.generated.resources.no_profile
import org.jetbrains.compose.resources.painterResource
import org.pascal.ecommerce.data.remote.dtos.user.UserInfo

@Composable
fun TopAppBarHeader(
    modifier: Modifier = Modifier,
    user: UserInfo? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Card(
            modifier = Modifier
                .width(50.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            shape = RoundedCornerShape(12.dp),
            colors =  CardDefaults.cardColors(Color.White)
        ) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = FeatherIcons.Menu,
                    contentDescription = ""
                )
            }
        }

        Card(
            modifier = Modifier
                .size(50.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            shape = RoundedCornerShape(12.dp),
            colors =  CardDefaults.cardColors(Color.White)
        ) {
            AsyncImage(
                imageLoader = getAsyncImageLoader(LocalPlatformContext.current),
                model = user?.photoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource(Res.drawable.no_profile),
                placeholder = painterResource(Res.drawable.no_profile),
                modifier = Modifier
                    .size(60.dp)
                    .background(MaterialTheme.colorScheme.background, CircleShape)
            )
        }

    }
}