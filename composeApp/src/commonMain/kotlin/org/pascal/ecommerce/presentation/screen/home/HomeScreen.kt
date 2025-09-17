package org.pascal.ecommerce.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.mutableIntStateOf
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
import ecommerce_kmp.composeapp.generated.resources.Res
import ecommerce_kmp.composeapp.generated.resources.close
import ecommerce_kmp.composeapp.generated.resources.filter_list
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.pascal.ecommerce.data.local.entity.ProductEntity
import org.pascal.ecommerce.data.preferences.PrefLogin
import org.pascal.ecommerce.data.remote.dtos.user.User
import org.pascal.ecommerce.presentation.component.dialog.ShowDialog
import org.pascal.ecommerce.presentation.component.form.Search
import org.pascal.ecommerce.presentation.component.screenUtils.LoadingScreen
import org.pascal.ecommerce.presentation.component.screenUtils.PullRefreshComponent
import org.pascal.ecommerce.presentation.component.screenUtils.TopAppBarHeader
import org.pascal.ecommerce.presentation.screen.home.state.HomeUIState
import org.pascal.ecommerce.presentation.screen.home.state.LocalHomeEvent
import org.pascal.ecommerce.theme.AppTheme
import org.pascal.ecommerce.utils.getAsyncImageLoader

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: HomeViewModel = koinInject<HomeViewModel>(),
    onDetail: (String?) -> Unit
) {
    val coroutine = rememberCoroutineScope()
    val event = LocalHomeEvent.current
    val pref = PrefLogin.getLoginResponse()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadCategory()
        viewModel.loadProduct()
    }

    Surface(
        modifier = modifier.padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        if (uiState.isLoading) LoadingScreen()

        if (uiState.isError.first) {
            ShowDialog(
                message = uiState.isError.second,
                textButton = stringResource(Res.string.close)
            ) {
                viewModel.setError(false)
            }
        }

        PullRefreshComponent(
            onRefresh = {
                viewModel.loadProduct()
            }
        ) {
            CompositionLocalProvider(
                LocalHomeEvent provides event.copy(
                    onSearch = {
                        viewModel.searchProduct(it)
                    },
                    onCategory = {
                        coroutine.launch {
                            if (viewModel.isOnline.value) {
                                viewModel.loadProduct(it)
                            } else {
//                                showToast(context, context.getString(R.string.connection_offline))
                            }
                        }
                    },
                    onFavorite = { isFav, item ->
                        coroutine.launch {
                            viewModel.saveFavorite(isFav, item)
                        }
                    },
                    onDetail = {
                        onDetail(it?.id.toString())
                    },
                )
            ) {
                Box {
                    HomeContent(
                        user = pref,
                        uiState = uiState
                    )
                }

            }
        }
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    user: User? = null,
    uiState: HomeUIState
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp)
        ) {

            TopAppBarHeader(user = user)

            Spacer(modifier = Modifier.padding(10.dp))

            OurProductsWithSearch()

            Spacer(modifier = Modifier.padding(16.dp))

            ProductCategory(category = uiState.category)

            Spacer(modifier = Modifier.padding(16.dp))

            ProductWidget(uiState = uiState)
        }
    }
}

@Composable
fun OurProductsWithSearch(
    modifier: Modifier = Modifier
) {
    val event = LocalHomeEvent.current

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            buildAnnotatedString {
                withStyle(style = ParagraphStyle(lineHeight = 30.sp)) {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 24.sp
                        )
                    ) {
                        append("Our\n")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    ) {
                        append("Products")
                    }
                }
            }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp)
                .padding(top = 30.dp)
        ) {
            Search(
                modifier = Modifier.weight(1f)
            ) {
                event.onSearch(it)
            }
            Spacer(modifier = Modifier.width(5.dp))
            Card(
                modifier = Modifier
                    .width(60.dp)
                    .padding(start = 16.dp)
                    .clickable { },
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(12.dp),
                colors =  CardDefaults.cardColors(Color.White)
            ) {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(Res.drawable.filter_list),
                        contentDescription = "Filter Icon",
                        modifier = Modifier.size(20.dp, 20.dp)
                    )

                }
            }
        }
    }
}

@Composable
fun ProductCategory(
    modifier: Modifier = Modifier,
    category: List<String>? = null
) {
    val event = LocalHomeEvent.current
    var isSelect by remember { mutableIntStateOf(-1) }

    LazyRow(
        modifier = modifier.fillMaxWidth()
    ) {
        itemsIndexed(category ?: emptyList()) { index, item ->

            if (index != 0) Spacer(Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        isSelect = index
                        event.onCategory(item)
                    }
                    .height(40.dp)
                    .border(
                        color = if (index == isSelect) MaterialTheme.colorScheme.primary 
                        else MaterialTheme.colorScheme.onSurface,
                        width = 2.dp,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item,
                    color = if (index == isSelect) MaterialTheme.colorScheme.primary else Color.LightGray
                )
            }
        }
    }
}

@Composable
fun ProductWidget(
    modifier: Modifier = Modifier,
    uiState: HomeUIState
) {
    val event = LocalHomeEvent.current

    LazyVerticalGrid(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxSize(),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(uiState.product ?: emptyList()) { item ->

            var isFavorite by remember { mutableStateOf(item.isFavorite ?: false) }

            Card(
                modifier = Modifier
                    .width(180.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors =  CardDefaults.cardColors(Color.White)
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
                        event.onFavorite(isFavorite, item)
                    }) {
                        Icon(
                            imageVector = if (isFavorite) FeatherIcons.Bookmark
                            else FeatherIcons.Bookmark,
                            contentDescription = "",
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
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
                            contentScale = ContentScale.Fit,
                            contentDescription = "",
                            imageLoader = getAsyncImageLoader(LocalPlatformContext.current),
                            model = item.thumbnail
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
                            text = item.title ?: "No Title",
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
                                        color = MaterialTheme.colorScheme.onSurface
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

        item {
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    AppTheme {
        HomeContent(
            uiState = HomeUIState(
                category = listOf("Category 1", "Category 2"),
                product = listOf(ProductEntity(), ProductEntity(), ProductEntity()),
            )
        )
    }
}
