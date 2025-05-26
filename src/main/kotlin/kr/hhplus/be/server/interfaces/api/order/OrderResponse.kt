package kr.hhplus.be.server.interfaces.api.order

import kr.hhplus.be.server.application.order.OrderResult

class OrderResponse {
    data class Order(
        val orderId: Long,
        val totalAmount: Int,
        val paymentId: Long,
        val remainBalance: Int,
    )

    companion object {
        fun from(result: OrderResult.Order): Order {
            return Order(
                orderId = result.orderId,
                totalAmount = result.totalAmount,
                paymentId = result.paymentId,
                remainBalance = result.remainBalance
            )
        }
    }

}