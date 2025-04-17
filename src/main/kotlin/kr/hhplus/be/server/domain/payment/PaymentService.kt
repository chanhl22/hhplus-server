package kr.hhplus.be.server.domain.payment

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PaymentService(
    private val paymentRepository: PaymentRepository
) {
    @Transactional
    fun process(command: PaymentCommand.Pay): Payment {
        val order = command.order
        val payment = Payment.decide(order, command.balance)
        return paymentRepository.save(payment)
    }

}