package kr.hhplus.be.server.domain.payment

interface PaymentEventPublisher {

    fun publish(event: PaymentEvent.Completed)

}