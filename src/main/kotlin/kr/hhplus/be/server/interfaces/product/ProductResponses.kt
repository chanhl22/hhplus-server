package kr.hhplus.be.server.interfaces.product

import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.product.ProductDomains.ProductSalesInfo

@Suppress("unused")
class ProductResponses {
    data class ProductResponse(
        val productId: Long,
        val name: String,
        val price: Int,
        val quantity: Int,
        val description: String
    ) {
        companion object {
            fun from(product: Product): ProductResponse {
                return ProductResponse(
                    productId = product.id,
                    name = product.name,
                    price = product.price,
                    quantity = product.stock.quantity,
                    description = product.description
                )
            }
        }
    }

    data class TopSellingProductResponse(
        val rank: Int,
        val productId: Long,
        val name: String,
        val price: Int,
        val soldQuantity: Int,
        val stock: Int
    ) {
        companion object {
            fun from(info: ProductSalesInfo): TopSellingProductResponse {
                val product = info.product
                val stock = product.stock
                return TopSellingProductResponse(
                    rank = info.rank,
                    productId = product.id,
                    name = product.name,
                    price = product.price,
                    soldQuantity = info.totalSales,
                    stock = stock.quantity
                )
            }
        }
    }

}