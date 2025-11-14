package org.pascal.ecommerce.presentation.screen.profile

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import compose.icons.FeatherIcons
import compose.icons.feathericons.CheckCircle
import compose.icons.feathericons.X
import ecommerce_kmp.composeapp.generated.resources.Res
import ecommerce_kmp.composeapp.generated.resources.close
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.pascal.ecommerce.data.local.entity.CartEntity
import org.pascal.ecommerce.data.preferences.PrefLogin
import org.pascal.ecommerce.data.remote.dtos.user.UserInfo
import org.pascal.ecommerce.domain.model.TransactionModel
import org.pascal.ecommerce.presentation.component.button.ButtonComponent
import org.pascal.ecommerce.presentation.component.dialog.ShowDialog
import org.pascal.ecommerce.presentation.component.screenUtils.DynamicAsyncImage
import org.pascal.ecommerce.presentation.component.screenUtils.LoadingScreen
import org.pascal.ecommerce.presentation.screen.profile.state.LocalProfileEvent
import org.pascal.ecommerce.presentation.screen.profile.state.ProfileUIState
import org.pascal.ecommerce.theme.AppTheme

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: ProfileViewModel = koinInject<ProfileViewModel>(),
    onVerified: () -> Unit,
    onLogout: () -> Unit
) {
    val coroutine = rememberCoroutineScope()
    val event = LocalProfileEvent.current
    val pref: UserInfo? = PrefLogin.getLoginResponse()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadTransaction(pref?.uid ?: "")
        viewModel.loadVerified(pref?.uid ?: "")
    }

    CompositionLocalProvider(
        LocalProfileEvent provides event.copy(
            onVerified = {
                onVerified()
            },
            onLogout = {
                coroutine.launch {
                    viewModel.loadLogout()
                    onLogout()
                }
            }
        )
    ) {
        Surface(
            modifier = modifier.padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
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

            ProfileContent(
                pref = pref,
                uiState = uiState
            )
        }
    }

}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    pref: UserInfo? = null,
    uiState: ProfileUIState
) {
    val event = LocalProfileEvent.current

    Box {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    DynamicAsyncImage(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(60.dp),
                        imageUrl = pref?.photoUrl.orEmpty()
                    )

                    if (uiState.isVerified) {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(24.dp),
                            imageVector = FeatherIcons.CheckCircle,
                            contentDescription = null,
                            tint = Color.Green
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        modifier = Modifier,
                        text = pref?.displayName ?: "No Name",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 20.sp,
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = pref?.email ?: "No Email",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 16.sp,
                        ),
                        color = Color.Gray
                    )
                }
            }

            if (!uiState.isVerified) {
                Box(
                    modifier = modifier
                        .padding(top = 24.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { event.onVerified() }
                        .padding(vertical = 10.dp, horizontal = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "No Verified",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = FeatherIcons.X,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                buildAnnotatedString {
                    withStyle(style = ParagraphStyle(lineHeight = 30.sp)) {
                        withStyle(
                            style = SpanStyle(
                                color = Color.LightGray,
                                fontSize = 24.sp
                            )
                        ) {
                            append("History\n")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        ) {
                            append("Transaction")
                        }

                    }
                }
            )

            Spacer(modifier = Modifier.padding(10.dp))

            TransactionItemList(
                uiState = uiState
            )
        }
        ButtonComponent(
            modifier = Modifier
                .background(Color.White)
                .padding(20.dp)
                .align(Alignment.BottomCenter),
            text = "Logout"
        ) {
            event.onLogout()
        }
    }
}

@Composable
fun TransactionItemList(
    modifier: Modifier = Modifier,
    uiState: ProfileUIState,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 5.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        itemsIndexed(uiState.transactionList) { i, it ->
            TransactionItemText(
                title = "Date",
                value = it.date ?: "-"
            )

            Spacer(Modifier.height(8.dp))

            TransactionItems(
                modifier = Modifier,
                backgroundColor = MaterialTheme.colorScheme.background,
                item = it
            )

            Spacer(Modifier.height(8.dp))

            TransactionItemText(
                title = "Total",
                value = "$${it.total}",
                isTotal = true
            )

            HorizontalDivider(modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Composable
fun TransactionItems(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    item: TransactionModel? = null
) {
    item?.products?.forEach { product ->
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
                DynamicAsyncImage(
                    modifier = Modifier.padding(8.dp),
                    imageUrl = product.imageID.orEmpty()
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
                    text = product.name ?: "",
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
                                append("$")
                            }
                            withStyle(
                                style = SpanStyle(
                                    MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                append(product.price.toString())
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
                            text = product.qty.toString(),
                            fontSize = 14.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItemText(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    isTotal: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (isTotal) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                fontSize = if (isTotal) 16.sp else 12.sp
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall.copy(
                color = if (isTotal) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary,
                fontSize = if (isTotal) 16.sp else 12.sp
            ),
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun ProfilePreview() {
    AppTheme {
        ProfileContent(
            uiState = ProfileUIState(
                transactionList = listOf(
                    TransactionModel(products = listOf(CartEntity()))
                )
            ),
        )
    }
}