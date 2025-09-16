package org.pascal.ecommerce.data.remote.dtos.user

@kotlinx.serialization.Serializable
data class User(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val photo_url: String? = null
)
