package kr.hhplus.be.server.interfaces.product

import kr.hhplus.be.server.domain.product.ProductDomains.ProductSalesInfo
import kr.hhplus.be.server.domain.product.ProductInfo

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
            fun from(info: ProductInfo.Find): ProductResponse {
                return ProductResponse(
                    productId = info.productId,
                    name = info.name,
                    price = info.price,
                    quantity = info.quantity,
                    description = info.description
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
    ) {
        companion object {
            fun from(info: ProductSalesInfo): TopSellingProductResponse {
                return TopSellingProductResponse(
                    rank = info.rank,
                    productId = info.productId,
                    name = info.name,
                    price = info.price,
                    soldQuantity = info.soldQuantity,
                )
            }
        }
    }

}