package kr.hhplus.be.server.interfaces.api.product

import kr.hhplus.be.server.domain.product.ProductInfo

class ProductResponse {
    data class Find(
        val productId: Long,
        val name: String,
        val price: Int,
        val quantity: Int,
        val description: String
    )

    data class FindTopSales(
        val rank: Int,
        val productId: Long,
        val name: String,
        val price: Int,
        val soldQuantity: Int
    )

    companion object {
        fun from(info: ProductInfo.Find): Find {
            return Find(
                productId = info.productId,
                name = info.name,
                price = info.price,
                quantity = info.quantity,
                description = info.description
            )
        }

        fun from(info: ProductInfo.FindTopSales): FindTopSales {
            return FindTopSales(
                rank = info.rank,
                productId = info.productId,
                name = info.name,
                price = info.price,
                soldQuantity = info.soldQuantity,
            )
        }
    }

}