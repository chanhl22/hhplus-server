package kr.hhplus.be.server.fixture.payment

import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.payment.PaymentCommands.PaymentCommand
import kr.hhplus.be.server.domain.user.User
import kr.hhplus.be.server.fixture.order.OrderDomainFixture
import kr.hhplus.be.server.fixture.user.UserDomainFixture

object PaymentCommandFixture {
    fun create(
        user: User = UserDomainFixture.create(),
        order: Order = OrderDomainFixture.create()
    ): PaymentCommand {
        return PaymentCommand(
            order = order,
            user = user
        )
    }
}