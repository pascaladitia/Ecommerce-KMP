package org.pascal.ecommerce.data.remote.dtos.rate

import kotlinx.serialization.Serializable

@Serializable
data class UsdRateResponse(
    val rates: Map<String, Double>
)