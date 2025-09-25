package org.pascal.ecommerce.data.preferences

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.serialization.json.Json
import org.pascal.ecommerce.createSettings
import org.pascal.ecommerce.data.remote.dtos.user.UserInfo

object PrefLogin {
    private const val IS_LOGIN = "is_login"
    private const val RESPONSE_LOGIN = "response_login"

    private fun Settings.setLoginResponse(value: UserInfo?) {
        if (value != null) {
            val jsonString = Json.encodeToString(value)
            putString(RESPONSE_LOGIN, jsonString)
        } else {
            remove(RESPONSE_LOGIN)
        }
    }

    private fun Settings.getLoginResponse(): UserInfo? {
        val jsonString = getString(RESPONSE_LOGIN, "")
        return jsonString.let { Json.decodeFromString(it) }
    }

    fun setLoginResponse(value: UserInfo?) {
        createSettings().setLoginResponse(value)
    }

    fun getLoginResponse(): UserInfo? {
        return try {
            createSettings().getLoginResponse()
        } catch (e: Exception) {
            null
        }
    }

    fun setIsLogin(value: Boolean) {
        createSettings()[IS_LOGIN] = value
    }

    fun getIsLogin(): Boolean {
        return createSettings()[IS_LOGIN, false]
    }

    fun clear() {
        createSettings().clear()
    }
}