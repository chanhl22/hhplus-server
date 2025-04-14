package kr.hhplus.be.server.domain.payment

import org.springframework.stereotype.Service
import kr.hhplus.be.server.domain.payment.PaymentCommands.PaymentCommand

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository
) {
    fun process(command: PaymentCommand): Payment {
        val user = command.user
        val order = command.order
        val payment = Payment.decide(order, user.point.balance)
        return paymentRepository.save(payment)
    }
}