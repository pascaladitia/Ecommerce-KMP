package org.pascal.ecommerce.data.repository

import org.pascal.ecommerce.data.remote.KtorClientApi
import org.koin.core.annotation.Single
import org.pascal.ecommerce.data.remote.dtos.BaseResponse

@Single
class BaseRepository : BaseRepositoryImpl {
    override suspend fun postApi(body: String): BaseResponse {
        return KtorClientApi.postApi(body)
    }
}