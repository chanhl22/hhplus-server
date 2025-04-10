package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.domain.order.Order

class PaymentCommands {
    data class PaymentCommand(
        val order: Order
    ) {
        companion object {
            fun of(order: Order): PaymentCommand {
                return PaymentCommand(
                    order = order
                )
            }
        }
    }
}