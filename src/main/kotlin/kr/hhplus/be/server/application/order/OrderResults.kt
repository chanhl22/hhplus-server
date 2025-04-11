package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.payment.Payment
import kr.hhplus.be.server.domain.user.User

class OrderResults {
    data class OrderResult (
        val user: User,
        val order: Order,
        val payment: Payment
    ) {
        companion object {
            fun of(user: User, order: Order, payment: Payment): OrderResult {
                return OrderResult(
                    user = user,
                    order = order,
                    payment = payment
                )
            }
        }
    }
}
