package org.pascal.ecommerce.data.preferences

import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import org.pascal.ecommerce.createSettings

object PrefCategories {
    private const val IS_CATEGORY = "is_category"
    private const val RESPONSE_CATEGORY = "response_category"

    private fun Settings.setCategoriesResponse(value: List<String>?) {
        if (value != null) {
            val jsonString = Json.encodeToString(value)
            putString(RESPONSE_CATEGORY, jsonString)
        } else {
            remove(RESPONSE_CATEGORY)
        }
    }

    private fun Settings.getCategoriesResponse(): List<String>? {
        val jsonString = getString(RESPONSE_CATEGORY, "")
        return jsonString.let { Json.decodeFromString(it) }
    }

    fun setCategoriesResponse(value: List<String>?) {
        createSettings().setCategoriesResponse(value)
    }

    fun getCategoriesResponse(): List<String>? {
        return createSettings().getCategoriesResponse()
    }

    fun clear() {
        createSettings().clear()
    }
}