package org.pascal.ecommerce.presentation.screen.cart

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import compose.icons.FeatherIcons
import compose.icons.feathericons.Delete
import ecommerce_kmp.composeapp.generated.resources.Res
import ecommerce_kmp.composeapp.generated.resources.connection_offline
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.pascal.ecommerce.data.local.entity.CartEntity
import org.pascal.ecommerce.data.remote.dtos.user.User
import org.pascal.ecommerce.presentation.component.screenUtils.TopAppBarHeader
import org.pascal.ecommerce.presentation.screen.cart.component.CartPayment
import org.pascal.ecommerce.presentation.screen.cart.state.LocalCartEvent
import org.pascal.ecommerce.theme.AppTheme
import org.pascal.ecommerce.utils.calculateTotalPrice
import org.pascal.ecommerce.utils.getAsyncImageLoader
import org.pascal.ecommerce.utils.isOnline
import org.pascal.ecommerce.utils.showToast

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: CartViewModel = koinInject<CartViewModel>(),
    onFinish: (List<CartEntity?>?) -> Unit
) {
    val coroutine = rememberCoroutineScope()
    val event = LocalCartEvent.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var snapUrl by remember { mutableStateOf<String?>(null) }
    var listProduct by remember { mutableStateOf<List<CartEntity?>?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getCart()
    }

    CompositionLocalProvider(
        LocalCartEvent provides event.copy(
            onDelete = {
                coroutine.launch {
                    viewModel.deleteCart()
                }
            },
            onNext = {
                coroutine.launch {
                    if (isOnline()) {
                        listProduct = it
                        snapUrl = viewModel.createSnapTransaction(calculateTotalPrice(it).toDouble())
                    } else {
                        showToast(getString(Res.string.connection_offline))
                    }
                }
            }
        )
    ) {
        Surface(
            modifier = modifier.padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            if (snapUrl == null) {
                CartContent(
                    product = uiState.product
                )
            } else {
                CartPayment(
                    snapUrl ?: ""
                ) {
                    onFinish(listProduct)
                }
            }
        }
    }
}

@Composable
fun CartContent(
    user: User? = null,
    product: List<CartEntity> = emptyList()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            TopAppBarHeader(user = user)
            Spacer(modifier = Modifier.padding(5.dp))
            DeleteCart()
            Spacer(modifier = Modifier.padding(20.dp))
            CartItemList(product = product)
            Spacer(modifier = Modifier.padding(20.dp))
        }

        NextButtonWithTotalItems(
            modifier = Modifier.align(Alignment.BottomCenter),
            product = product
        )
    }
}


@Composable
fun DeleteCart(
    modifier: Modifier = Modifier
) {
    val event = LocalCartEvent.current

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
                        append("Cart")
                    }

                }
            }
        )

        IconButton(onClick = { event.onDelete() }) {
            Icon(
                imageVector = FeatherIcons.Delete,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )

        }
    }
}

@Composable
fun CartItemList(
    modifier: Modifier = Modifier,
    product: List<CartEntity>,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp),
        contentPadding = PaddingValues(horizontal = 5.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        items(product) {
            ProductCartItems(
                modifier = Modifier,
                imagePainter = it.imageID ?: "",
                title = it.name ?: "",
                price = it.price.toString(),
                pricetag = "$",
                count = it.qty.toString(),
                backgroundColor = Color.LightGray
            )
        }
    }
}

@Composable
fun ProductCartItems(
    modifier: Modifier = Modifier,
    imagePainter: String = "",
    title: String = "",
    price: String = "",
    pricetag: String = "",
    count: String = "",
    backgroundColor: Color = Color.Transparent,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .fillMaxWidth(0.2f)
                .clip(RoundedCornerShape(20.dp))
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                imageLoader = getAsyncImageLoader(LocalPlatformContext.current),
                model = imagePainter,
                contentDescription = "",
                modifier = Modifier.padding(8.dp),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(pricetag)
                        }
                        withStyle(
                            style = SpanStyle(
                                MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            append(price)
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier,
                    fontSize = 16.sp

                )
                Box(
                    modifier = Modifier
                        .size(35.dp, 35.dp)
                        .clip(CircleShape)
                        .background(backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = count,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

        }
    }
}

@Composable
fun NextButtonWithTotalItems(
    modifier: Modifier = Modifier,
    product: List<CartEntity>
) {
    val event = LocalCartEvent.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        HorizontalDivider(color = Color.LightGray, thickness = 2.dp)
        Spacer(modifier = Modifier.padding(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${product.size} Items",
                fontSize = 14.sp,
                color = Color.LightGray
            )

            Text(
                text = "$${calculateTotalPrice(product)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = {
                event.onNext(product)
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 30.dp,
                    bottom = 34.dp
                )
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Next",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun CartPreview() {
    AppTheme {
        CartContent()
    }
}