package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.domain.payment.Payment
import kr.hhplus.be.server.domain.point.Point

class OrderResult {
    data class Order(
        val orderId: Long,
        val totalAmount: Int,
        val paymentId: Long,
        val remainBalance: Int
    )

    companion object {
        fun of(point: Point, order: kr.hhplus.be.server.domain.order.Order, payment: Payment): Order {
            return Order(
                orderId = order.id!!,
                totalAmount = order.totalPrice,
                paymentId = payment.id!!,
                remainBalance = point.balance
            )
        }
    }

}
