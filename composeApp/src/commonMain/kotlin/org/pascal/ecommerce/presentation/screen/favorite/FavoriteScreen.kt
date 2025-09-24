package org.pascal.ecommerce.presentation.screen.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import compose.icons.FeatherIcons
import compose.icons.feathericons.Bookmark
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.pascal.ecommerce.data.local.entity.FavoriteEntity
import org.pascal.ecommerce.data.preferences.PrefLogin
import org.pascal.ecommerce.data.remote.dtos.user.User
import org.pascal.ecommerce.presentation.component.screenUtils.TopAppBarHeader
import org.pascal.ecommerce.presentation.screen.favorite.state.FavoriteUIState
import org.pascal.ecommerce.presentation.screen.favorite.state.LocalFavoriteEvent
import org.pascal.ecommerce.theme.AppTheme
import org.pascal.ecommerce.utils.getAsyncImageLoader

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: FavoriteViewModel = koinInject<FavoriteViewModel>(),
    onDetail: (FavoriteEntity?) -> Unit
) {
    val coroutine = rememberCoroutineScope()
    val event = LocalFavoriteEvent.current

    val pref = PrefLogin.getLoginResponse()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadFavorite()
    }

    CompositionLocalProvider(
        LocalFavoriteEvent provides event.copy(
            onDetail = {
                onDetail(it)
            },
            onDelete = {
                coroutine.launch {
                    viewModel.delete(it)
                }
            }
        )
    ) {
        Surface(
            modifier = modifier.padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            FavoriteContent(
                user = pref,
                uiState = uiState
            )
        }

    }
}

@Composable
fun FavoriteContent(
    user: User? = null,
    uiState: FavoriteUIState
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            TopAppBarHeader(user = user)
            Spacer(modifier = Modifier.padding(5.dp))
            DeleteFavorite()
            Spacer(modifier = Modifier.padding(20.dp))
            FavoriteItemList(uiState = uiState)
            Spacer(modifier = Modifier.padding(20.dp))
        }
    }
}


@Composable
fun DeleteFavorite(
    modifier: Modifier = Modifier
) {
    val event = LocalFavoriteEvent.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            buildAnnotatedString {
                withStyle(style = ParagraphStyle(lineHeight = 30.sp)) {
                    withStyle(
                        style = SpanStyle(
                            color = Color.LightGray,
                            fontSize = 24.sp
                        )
                    ) {
                        append("Shopping\n")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    ) {
                        append("Favorite")
                    }

                }
            }
        )
    }
}

@Composable
fun FavoriteItemList(
    modifier: Modifier = Modifier,
    uiState: FavoriteUIState,
) {
    val event = LocalFavoriteEvent.current

    LazyVerticalGrid(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxSize(),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(uiState.product ?: emptyList()) { item ->

            var isFavorite by remember { mutableStateOf(true) }

            Card(
                modifier = Modifier
                    .width(180.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { event.onDetail(item) }
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(12.dp)
                ) {
                    IconButton(onClick = {
                        isFavorite = !isFavorite
                        event.onDelete(item)
                    }) {
                        Icon(
                            imageVector = if (isFavorite) FeatherIcons.Bookmark
                            else FeatherIcons.Bookmark,
                            contentDescription = "",
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(100.dp),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = "",
                            imageLoader = getAsyncImageLoader(LocalPlatformContext.current),
                            model = item.imageID
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = item.name ?: "No Title",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = item.category ?: "No Category",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("$")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        MaterialTheme.colorScheme.onPrimary
                                    )
                                ) {
                                    append(item.price.toString())
                                }
                            },
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier,
                            fontSize = 16.sp

                        )
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritePreview() {
    AppTheme {
        FavoriteContent(
            uiState = FavoriteUIState(
                product = listOf(FavoriteEntity(), FavoriteEntity())
            )
        )
    }
}