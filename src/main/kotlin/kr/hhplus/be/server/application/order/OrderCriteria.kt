package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.domain.order.OrderCommand

class OrderCriteria {
    data class Order(
        val userId: Long,
        val products: List<OrderProduct>,
        val couponId: Long?
    ) {
        @Suppress("unused")
        fun toLockKeys(): List<String> {
            return products.map { it.productId }
                .distinct()
                .sorted()
                .map { productId -> productId.toString() }
        }

        fun toCommand(pointId: Long): OrderCommand.Create {
            return OrderCommand.Create(
                userId = userId,
                pointId = pointId,
                products = products.map { product ->
                    OrderCommand.OrderProduct(
                        product.productId,
                        product.quantity
                    )
                },
                couponId = couponId
            )
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