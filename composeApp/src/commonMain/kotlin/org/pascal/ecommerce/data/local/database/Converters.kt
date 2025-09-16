package org.pascal.ecommerce.data.local.database

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import org.pascal.ecommerce.data.remote.dtos.ReviewResonse

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromReviewList(reviews: List<ReviewResonse>?): String? {
        return reviews?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toReviewList(reviewsString: String?): List<ReviewResonse>? {
        return reviewsString?.let { json.decodeFromString(it) } ?: emptyList()
    }

    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toStringList(listString: String?): List<String>? {
        return listString?.let { json.decodeFromString(it) } ?: emptyList()
    }
}