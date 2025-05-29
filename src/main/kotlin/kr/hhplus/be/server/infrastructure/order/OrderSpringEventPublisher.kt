package kr.hhplus.be.server.infrastructure.order

import kr.hhplus.be.server.domain.order.OrderEvent
import kr.hhplus.be.server.domain.order.OrderEventPublisher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class OrderSpringEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val kafkaTemplate: KafkaTemplate<String, OrderEvent.Completed>
) : OrderEventPublisher {

    override fun publish(event: OrderEvent.Completed) {
        applicationEventPublisher.publishEvent(event)
        kafkaTemplate.send("order.completed", event)
    }

    override fun publish(event: OrderEvent.Create) {
        applicationEventPublisher.publishEvent(event)
    }

}