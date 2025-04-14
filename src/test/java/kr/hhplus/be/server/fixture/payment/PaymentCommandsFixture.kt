package kr.hhplus.be.server.fixture.payment

import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.payment.PaymentCommands.PaymentCommand
import kr.hhplus.be.server.domain.user.User
import kr.hhplus.be.server.fixture.order.OrderDomainsFixture
import kr.hhplus.be.server.fixture.user.UserFixture

object PaymentCommandFixture {
    fun create(
        user: User = UserFixture.create(),
        order: Order = OrderDomainsFixture.create()
    ): PaymentCommand {
        return PaymentCommand(
            order = order,
            user = user
        )
    }
}