package kr.hhplus.be.server.domain.product

class ProductCommand {
    data class Deduct(
        val products: List<OrderProduct>,
    ) {
        fun getProductIds(): List<Long> {
            return products.map { product ->
                product.productId
            }
        }
    }

    data class OrderProduct(
        val productId: Long,
        val quantity: Int
    )

    data class ProductStatistics(
        val productId: Long,
        val totalSales: Int
    )

    companion object {
        fun of(orderProducts: List<OrderProduct>): Deduct {
            return Deduct(products = orderProducts)
        }

        fun of(productId: Long, totalSales: Int): ProductStatistics {
            return ProductStatistics(
                productId = productId,
                totalSales = totalSales
            )
        }
    }

}