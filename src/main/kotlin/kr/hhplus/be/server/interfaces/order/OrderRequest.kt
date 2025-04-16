package kr.hhplus.be.server.interfaces.order

import kr.hhplus.be.server.application.order.OrderCriteria

@Suppress("unused")
class OrderRequest {
    data class Order(
        val products: List<OrderProduct>,
        val couponId: Long?
    ) {
        fun toCriteria(userId: Long): OrderCriteria.Order {
            return OrderCriteria.of(
                userId = userId,
                products = products.map { product ->
                    OrderCriteria.OrderProduct(
                        productId = product.productId,
                        quantity = product.quantity
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
}