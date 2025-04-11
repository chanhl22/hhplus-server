package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.domain.order.Order
import org.springframework.stereotype.Component

@Component
class PaymentFactory {
    fun create(order: Order): Payment {
        return Payment.create(order)
    }
}