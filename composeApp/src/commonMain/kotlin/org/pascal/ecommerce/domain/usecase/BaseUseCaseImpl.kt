package org.pascal.ecommerce.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.pascal.ecommerce.domain.model.BaseModel

interface BaseUseCaseImpl {
    suspend fun postApi(body: String): Flow<BaseModel>
}