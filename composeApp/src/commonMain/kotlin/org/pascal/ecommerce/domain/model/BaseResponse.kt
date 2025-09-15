package org.pascal.ecommerce.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse(

	@SerialName("code")
	val code: String? = null,

	@SerialName("desc")
	val desc: String? = null,

	@SerialName("message")
	val message: String? = null
)