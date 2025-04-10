package kr.hhplus.be.server.domain.payment

import org.springframework.stereotype.Service
import kr.hhplus.be.server.domain.payment.PaymentCommands.PaymentCommand

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val paymentFactory: PaymentFactory
) {
    fun save(command: PaymentCommand): Payment {
        val payment = paymentFactory.create(command.order)
        return paymentRepository.save(payment)
    }
}