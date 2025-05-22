package kr.hhplus.be.server.domain.payment

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val paymentEventPublisher: PaymentEventPublisher
) {

    @Transactional
    fun process(command: PaymentCommand.Save): Long {
        val payment = Payment.create(command.orderId, command.totalPrice)
        val savedPayment = paymentRepository.save(payment)

        paymentEventPublisher.publish(command.toEvent())

        return savedPayment.id
    }

}