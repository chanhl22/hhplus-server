package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.domain.product.ProductCommand

class OrderCriteria {
    data class Order(
        val userId: Long,
        val products: List<OrderProduct>,
        val couponId: Long?
    ) {
        fun toProductCommand(): ProductCommand.OrderProducts {
            return ProductCommand.of(
                products = products.map { product ->
                    ProductCommand.OrderProduct(
                        productId = product.productId,
                        quantity = product.quantity,
                    )
                }
            )
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