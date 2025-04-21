package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.point.Point

class PaymentCommand {
    data class Pay(
        val balance: Int,
        val order: Order
    )

    companion object {
        fun of(point: Point, order: Order): Pay {
            return Pay(
                balance = point.balance,
                order = order
            )
        }
    }

}