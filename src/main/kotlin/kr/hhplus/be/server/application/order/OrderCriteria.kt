package kr.hhplus.be.server.application.order

class OrderCriteria {
    data class Order(
        val userId: Long,
        val products: List<OrderProduct>,
        val couponId: Long?
    ) {
        fun getProductIds() : List<Long> {
            return products.map { product ->
                product.productId
            }
        }

        fun createOrderProductQuantityCountMap(): Map<Long, Int> {
            return products.associate { it.productId to it.quantity }
        }
    }

    data class OrderProduct(
        val productId: Long,
        val quantity: Int
    )

    companion object {
        fun of(userId: Long, products: List<OrderProduct>, couponId: Long?): Order {
            return Order(userId, products, couponId)
        }
    }

}