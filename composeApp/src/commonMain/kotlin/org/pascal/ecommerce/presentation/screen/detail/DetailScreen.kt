package org.pascal.ecommerce.presentation.screen.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import compose.icons.FeatherIcons
import compose.icons.feathericons.ShoppingCart
import ecommerce_kmp.composeapp.generated.resources.Res
import ecommerce_kmp.composeapp.generated.resources.close
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.pascal.ecommerce.domain.model.Product
import org.pascal.ecommerce.presentation.component.dialog.ShowDialog
import org.pascal.ecommerce.presentation.component.screenUtils.DynamicAsyncImage
import org.pascal.ecommerce.presentation.component.screenUtils.LoadingScreen
import org.pascal.ecommerce.presentation.component.screenUtils.RatingBar
import org.pascal.ecommerce.presentation.component.screenUtils.TopAppBarWithBack
import org.pascal.ecommerce.presentation.screen.detail.state.DetailUIState
import org.pascal.ecommerce.presentation.screen.detail.state.LocalDetailEvent
import org.pascal.ecommerce.theme.AppTheme
import org.pascal.ecommerce.utils.base.checkChannelValue

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    productId: String? = "",
    viewModel: DetailViewModel = koinInject<DetailViewModel>(),
    onNavBack: () -> Unit
) {
    val coroutine = rememberCoroutineScope()
    val event = LocalDetailEvent.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val nextEvent = remember { viewModel.nextEvent }

    LaunchedEffect(Unit) {
        viewModel.loadProductsDetail(productId)
    }

    LaunchedEffect(Unit) {
        nextEvent.checkChannelValue(onSuccess = { onNavBack() })
    }

    if (uiState.isLoading) LoadingScreen()
    if (uiState.isError) {
        ShowDialog(
            message = uiState.message,
            textButton = stringResource(Res.string.close),
            color = MaterialTheme.colorScheme.primary
        ) {
            viewModel.setError(false)
        }
    }

    Surface(
        modifier = modifier.padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        CompositionLocalProvider(
            LocalDetailEvent provides event.copy(
                onCart = {
                    coroutine.launch {
                        viewModel.getCart(it)
                    }
                },
                onFavorite = { isFav, item ->
                    coroutine.launch {
                        viewModel.saveFavorite(isFav, item)
                    }
                },
                onNavBack = {
                    onNavBack()
                }
            )
        ) {
            DetailContent(
                uiState = uiState
            )
        }
    }
}

@Composable
fun DetailContent(
    modifier: Modifier = Modifier,
    uiState: DetailUIState
) {
    val event = LocalDetailEvent.current
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.product) {
        isFavorite = uiState.product?.isFavorite ?: false
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBarWithBack(
                isFavorite = isFavorite,
                onFavorite = {
                    isFavorite = !isFavorite
                    event.onFavorite(isFavorite, uiState.product)
                },
                onBackClick = {
                    event.onNavBack()
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { event.onCart(uiState.product) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = FeatherIcons.ShoppingCart,
                    contentDescription = "Add To Cart",
                    tint = Color.White
                )
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                ConstraintLayout(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val (imagesliderref, addtocartref) = createRefs()

                    Box(modifier = Modifier
                        .height(280.dp)
                        .constrainAs(imagesliderref) {
                            top.linkTo(imagesliderref.top)
                            bottom.linkTo(imagesliderref.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    ) {
                        HeaderImagesSlider(
                            product = uiState.product
                        )
                    }

                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(40.dp).copy(
                                bottomStart = ZeroCornerSize,
                                bottomEnd = ZeroCornerSize
                            ),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 300.dp)
                            .constrainAs(addtocartref) {
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(30.dp)
                        ) {
                            ProductTitle(product = uiState.product)

                            Spacer(modifier = Modifier.padding(10.dp))

                            ProductItemColorWithDesc(product = uiState.product)

                            Spacer(modifier = Modifier.padding(10.dp))

                            ProductAvailableSize(product = uiState.product)

                            Spacer(modifier = Modifier.padding(10.dp))

                            ProductReviews(product = uiState.product)

                            Spacer(modifier = Modifier.padding(60.dp))
                        }
                    }
                }
            }
        }
    )

}


@Composable
fun HeaderImagesSlider(
    modifier: Modifier = Modifier,
    product: Product? = null
) {
    var isSelect by remember { mutableIntStateOf(0) }
    var showImage by remember { mutableStateOf(product?.images?.get(0)) }

    LaunchedEffect(product) {
        showImage = product?.images?.get(0)
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        DynamicAsyncImage(
            modifier = Modifier.size(230.dp),
            imageUrl = showImage.orEmpty()
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(product?.images ?: emptyList()) { index, item ->
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .width(62.dp)
                        .border(
                            color = if (index == isSelect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
                            width = 2.dp,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            isSelect = index
                            showImage = item
                        },
                    contentAlignment = Alignment.Center
                ) {
                    DynamicAsyncImage(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(6.dp),
                        imageUrl = item
                    )

                }
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Composable
fun ProductTitle(
    modifier: Modifier = Modifier,
    product: Product? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .height(4.dp)
                .width(40.dp)
        )

        Spacer(modifier = Modifier.padding(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = product?.title ?: "",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Column(modifier = Modifier.wrapContentHeight()) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("$ ")
                        }
                        withStyle(
                            style = SpanStyle(
                                MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            append(product?.price.toString())
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

@Composable
fun ProductAvailableSize(
    modifier: Modifier = Modifier,
    product: Product? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {

        Text(
            text = "Tags Product",
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.padding(10.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(product?.tags ?: emptyList()) { item ->
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(70.dp)
                        .border(
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            width = 2.dp,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { }) {
                    Text(
                        modifier = Modifier
                            .padding(18.dp),
                        text = item,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    )


                }
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Composable
fun ProductItemColorWithDesc(
    modifier: Modifier = Modifier,
    product: Product? = null
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Description",
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = product?.description ?: "",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ProductReviews(
    modifier: Modifier = Modifier,
    product: Product? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Reviews",
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.padding(10.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(product?.review ?: emptyList()) { item ->
                Box(
                    modifier = Modifier
                        .border(
                            color = MaterialTheme.colorScheme.surface,
                            width = 2.dp,
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = item.reviewerName ?: "No Name",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )

                        Text(
                            text = item.comment ?: "-",
                            fontWeight = FontWeight.Bold,
                            color = Color.LightGray,
                            fontSize = 12.sp
                        )

                        RatingBar(rating = item.rating?.toDouble() ?: 0.0)
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailPreview() {
    AppTheme {
        DetailContent(
            uiState = DetailUIState()
        )
    }
}