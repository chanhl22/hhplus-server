package kr.hhplus.be.server.interfaces.order

import kr.hhplus.be.server.application.order.OrderResults

@Suppress("unused")
class OrderResponses {
    data class OrderResponse(
        val orderId: Long,
        val totalAmount: Int,
        val paymentId: Long,
        val remainBalance: Int,
    ) {
        companion object {
            fun of(orderResult: OrderResults.OrderResult): OrderResponse {
                return OrderResponse(
                    orderId = orderResult.order.id,
                    totalAmount = orderResult.order.totalPrice,
                    paymentId = orderResult.payment.id!!,
                    remainBalance = orderResult.user.point.balance
                )
            }
        }
    }

}