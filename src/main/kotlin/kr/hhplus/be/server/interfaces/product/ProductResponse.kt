package kr.hhplus.be.server.interfaces.product

import kr.hhplus.be.server.domain.product.Product

class ProductResponse {
    data class FoundProduct(
        val productId: Long,
        val name: String,
        val price: Int,
        val quantity: Int,
        val description: String
    ) {
        companion object {
            fun from(product: Product): FoundProduct {
                return FoundProduct(
                    productId = product.id,
                    name = product.name,
                    price = product.price,
                    quantity = product.stock.quantity,
                    description = product.description
                )
            }
        }
    }
}