package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.user.User

@Suppress("unused")
class PaymentCommands {
    data class PaymentCommand(
        val user: User,
        val order: Order
    ) {
        companion object {
            fun of(user: User, order: Order): PaymentCommand {
                return PaymentCommand(
                    user = user,
                    order = order
                )
            }
        }
    }
}