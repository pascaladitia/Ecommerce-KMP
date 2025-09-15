package org.pascal.ecommerce.data.repository

import org.pascal.ecommerce.domain.model.BaseResponse

interface RepositoryImpl {
    suspend fun createDetector(body: String) : BaseResponse
}