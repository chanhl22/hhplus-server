package kr.hhplus.be.server.fixture.payment

import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.payment.PaymentCommands.PaymentCommand

object PaymentCommandFixture {
    fun create(
        order: Order
    ): PaymentCommand {
        return PaymentCommand(
            order = order
        )
    }
}