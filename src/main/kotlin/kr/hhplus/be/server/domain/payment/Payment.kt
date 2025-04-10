package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.domain.order.Order

class Payment(
    val id: Long = 0L,
    val order: Order,
    val amount: Int
) {
    companion object {
        fun create(
            order: Order
        ): Payment {
            return Payment(
                order = order,
                amount = order.totalPrice
            )
        }
    }
}
