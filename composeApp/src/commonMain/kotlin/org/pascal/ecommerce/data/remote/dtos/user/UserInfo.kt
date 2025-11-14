package org.pascal.ecommerce.data.remote.dtos.user

@kotlinx.serialization.Serializable
data class UserInfo(
    val uid: String? = null,
    val displayName: String? = null,
    val email: String? = null,
    val photoUrl: String? = null
)
