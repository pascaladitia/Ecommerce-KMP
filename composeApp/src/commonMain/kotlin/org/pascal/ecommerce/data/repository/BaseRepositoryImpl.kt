package org.pascal.ecommerce.data.repository

import org.pascal.ecommerce.data.remote.dtos.BaseResponse

interface BaseRepositoryImpl {
    suspend fun postApi(body: String) : BaseResponse
}