package kr.hhplus.be.server.fixture.payment

import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.payment.Payment
import kr.hhplus.be.server.domain.payment.PaymentCommand
import kr.hhplus.be.server.domain.payment.PaymentStatus
import kr.hhplus.be.server.fixture.order.OrderDomainFixture

object PaymentCommandFixture {
    fun create(
        balance: Int = 10000,
        order: Order = OrderDomainFixture.create()
    ): PaymentCommand.Pay {
        return PaymentCommand.Pay(
            balance = balance,
            order = order
        )
    }
}

object PaymentDomainFixture {
    fun create(
        paymentId: Long = 1L,
        order: Order = OrderDomainFixture.create(),
        amount: Int = 100,
        paymentStatus: PaymentStatus = PaymentStatus.SUCCEEDED
    ): Payment {
        return Payment(
            id = paymentId,
            order = order,
            amount = amount,
            paymentStatus = paymentStatus
        )
    }

}