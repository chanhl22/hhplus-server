package kr.hhplus.be.server.infrastructure.payment

import kr.hhplus.be.server.domain.payment.PaymentEvent
import kr.hhplus.be.server.domain.payment.PaymentEventPublisher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class PaymentSpringEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher
) : PaymentEventPublisher {

    override fun publish(event: PaymentEvent.Completed) {
        applicationEventPublisher.publishEvent(event)
    }

}