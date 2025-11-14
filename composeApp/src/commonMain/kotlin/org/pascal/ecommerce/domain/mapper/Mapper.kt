package org.pascal.ecommerce.domain.mapper

import org.pascal.ecommerce.data.local.entity.ProductEntity
import org.pascal.ecommerce.data.remote.dtos.product.ProductResponse
import org.pascal.ecommerce.data.remote.dtos.transaction.TransactionResponse
import org.pascal.ecommerce.domain.model.BaseProduct
import org.pascal.ecommerce.domain.model.Product
import org.pascal.ecommerce.domain.model.Order

fun ProductResponse.toDomain(): BaseProduct {
    return BaseProduct(
        limit = this.limit ?: 0,
        products = this.products?.map { it.toDomain() } as List<Product>,
        skip = this.skip ?: 0,
        total = this.total ?: 0
    )
}

fun ProductEntity.toDomain(): Product {
    return Product(
        id = this.id,
        availabilityStatus = this.availabilityStatus.orEmpty(),
        brand = this.brand.orEmpty(),
        category = this.category.orEmpty(),
        description = this.description.orEmpty(),
        discountPercentage = this.discountPercentage ?: 0.0,
        images = this.images ?: emptyList(),
        minimumOrderQuantity = this.minimumOrderQuantity ?: 0,
        price = this.price ?: 0.0,
        rating = this.rating ?: 0.0,
        returnPolicy = this.returnPolicy.orEmpty(),
        review = this.review ?: emptyList(),
        shippingInformation = this.shippingInformation.orEmpty(),
        sku = this.sku.orEmpty(),
        stock = this.stock ?: 0,
        tags = this.tags ?: emptyList(),
        thumbnail = this.thumbnail.orEmpty(),
        title = this.title.orEmpty(),
        warrantyInformation = this.warrantyInformation.orEmpty(),
        weight = this.weight ?: 0,
        isFavorite = this.isFavorite ?: false
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        availabilityStatus = this.availabilityStatus,
        brand = this.brand,
        category = this.category,
        description = this.description,
        discountPercentage = this.discountPercentage,
        images = this.images,
        minimumOrderQuantity = this.minimumOrderQuantity,
        price = this.price,
        rating = this.rating,
        returnPolicy = this.returnPolicy,
        review = this.review,
        shippingInformation = this.shippingInformation,
        sku = this.sku,
        stock = this.stock,
        tags = this.tags,
        thumbnail = this.thumbnail,
        title = this.title,
        warrantyInformation = this.warrantyInformation,
        weight = this.weight,
        isFavorite = this.isFavorite
    )
}

fun TransactionResponse.toDomain(): Order {
    return Order(
        redirectUrl = this.redirectUrl.orEmpty()
    )
}
