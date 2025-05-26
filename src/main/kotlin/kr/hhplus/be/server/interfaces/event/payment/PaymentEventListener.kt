package kr.hhplus.be.server.interfaces.event.payment

import kr.hhplus.be.server.domain.payment.PaymentService
import kr.hhplus.be.server.domain.point.PointEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class PaymentEventListener(
    private val paymentService: PaymentService
) {

    @EventListener
    fun handle(event: PointEvent.Completed) {
        val command = PaymentEventMapper.toCommand(event)
        paymentService.process(command)
    }

}