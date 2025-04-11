package kr.hhplus.be.server.interfaces.order

import kr.hhplus.be.server.application.order.OrderCriteria.OrderCriterion
import kr.hhplus.be.server.application.order.OrderCriteria.OrderProductCriterion

@Suppress("unused")
class OrderRequests {
    data class OrderRequest(
        val userId: Long,
        val products: List<OrderProductRequest>,
        val couponId: Long
    ) {
        fun toCriterion(): OrderCriterion {
            return OrderCriterion.of(
                userId = userId,
                products = products.map { product ->
                    OrderProductCriterion(
                        productId = product.productId,
                        quantity = product.quantity
                    )
                },
                couponId = couponId
            )
        }
    }

    data class OrderProductRequest(
        val productId: Long,
        val quantity: Int
    )
}