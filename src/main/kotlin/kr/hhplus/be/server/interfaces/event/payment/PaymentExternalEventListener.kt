package kr.hhplus.be.server.interfaces.event.payment

import kr.hhplus.be.server.domain.payment.PaymentService
import kr.hhplus.be.server.domain.point.PointEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class PaymentExternalEventListener(
    private val paymentService: PaymentService
) {

    @EventListener
    fun handle(event: PointEvent.Completed) {
        val command = PaymentExternalEventMapper.toCommand(event)
        paymentService.process(command)
    }

}