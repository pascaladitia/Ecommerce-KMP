package org.pascal.ecommerce.domain.mapper

import org.pascal.ecommerce.data.remote.dtos.BaseResponse
import org.pascal.ecommerce.domain.model.BaseModel

fun BaseResponse.toDomain(): BaseModel {
    return BaseModel(
        code = this.code.orEmpty(),
        desc = this.desc.orEmpty(),
        message = this.message.orEmpty()
    )
}