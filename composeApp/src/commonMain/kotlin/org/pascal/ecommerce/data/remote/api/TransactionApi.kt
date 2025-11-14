package org.pascal.ecommerce.data.remote.api

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.util.date.getTimeMillis
import io.ktor.util.encodeBase64
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.koin.core.annotation.Single
import org.pascal.ecommerce.data.remote.client
import org.pascal.ecommerce.data.remote.dtos.transaction.TransactionDetails
import org.pascal.ecommerce.data.remote.dtos.transaction.TransactionRequest
import org.pascal.ecommerce.data.remote.dtos.transaction.TransactionResponse
import org.pascal.ecommerce.utils.Constant
import org.pascal.ecommerce.utils.base.handleApi
import kotlin.math.roundToInt

@Single
object TransactionApi {

    suspend fun createSnapTransaction(amountInUSD: Double): TransactionResponse {
        val exchangeRate = getUsdToIdrRate()
        val amountInIDR = ((amountInUSD) * exchangeRate).roundToInt()

        val body = TransactionRequest(
            transactionDetails = TransactionDetails(
                orderId = "ORDER-${getTimeMillis()}",
                grossAmount = amountInIDR
            )
        )

        val authHeader = "Basic " + ("${Constant.MIDTRANS_SERVER}:").encodeBase64()

        return client.post(Constant.SNAP_URL) {
            contentType(ContentType.Application.Json)
            setBody(body)
            headers {
                append(HttpHeaders.Authorization, authHeader)
            }
        }.handleApi()
    }

    suspend fun getUsdToIdrRate(): Double {
        val response: JsonObject = client.get(Constant.USD_URL).body()
        val rates = response["rates"]?.jsonObject
        val idr = rates?.get("IDR")?.jsonPrimitive?.doubleOrNull
        return idr ?: 15000.0
    }
}