package kr.hhplus.be.server.domain.product

class ProductCommand {
    data class OrderProducts (
        val products: List<OrderProduct>
    ) {
        fun getProductIds() : List<Long> {
            return products.map { product ->
                product.productId
            }
        }
    }

    data class OrderProduct(
        val productId: Long,
        val quantity: Int
    )

    companion object {
        fun of(products: List<OrderProduct>): OrderProducts {
            return OrderProducts(
                products = products
            )
        }
    }

}
