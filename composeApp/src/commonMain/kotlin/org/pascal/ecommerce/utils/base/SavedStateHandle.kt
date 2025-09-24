package org.pascal.ecommerce.utils.base

import androidx.navigation.NavController
import kotlinx.serialization.json.Json

inline fun <reified T> saveToCurrentBackStack(
    navController: NavController,
    key: String,
    data: T
) {
    val jsonOrValue = try {
        Json.encodeToString(data)
    } catch (_: Exception) {
        data.toString()
    }
    navController.currentBackStackEntry?.savedStateHandle?.set(key, jsonOrValue)
}

inline fun <reified T> getFromPreviousBackStack(
    navController: NavController,
    key: String
): T? {
    val value = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>(key) ?: return null

    return try {
        Json.decodeFromString<T>(value)
    } catch (_: Exception) {
        @Suppress("UNCHECKED_CAST")
        when (T::class) {
            String::class -> value as T
            Int::class -> value.toIntOrNull() as? T
            Double::class -> value.toDoubleOrNull() as? T
            Float::class -> value.toFloatOrNull() as? T
            Long::class -> value.toLongOrNull() as? T
            Boolean::class -> value.toBooleanStrictOrNull() as? T
            else -> null
        }
    }
}
