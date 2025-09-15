package org.pascal.ecommerce.data.repository

import org.pascal.ecommerce.data.remote.KtorClientApi
import org.koin.core.annotation.Single
import org.pascal.ecommerce.domain.model.BaseResponse

@Single
class Repository : RepositoryImpl {
    override suspend fun createDetector(body: String): BaseResponse {
        return KtorClientApi.createDetector(body)
    }
}