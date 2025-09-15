package org.pascal.ecommerce.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single
import org.pascal.ecommerce.data.repository.BaseRepository
import org.pascal.ecommerce.domain.mapper.toDomain
import org.pascal.ecommerce.domain.model.BaseModel

@Single
class BaseUseCase(
    val repository: BaseRepository
) : BaseUseCaseImpl {
    override suspend fun postApi(body: String): Flow<BaseModel> {
        return flow {
            emit(repository.postApi(body).toDomain())
        }
    }
}