package kr.hhplus.be.server.infrastructure.coupon

import kr.hhplus.be.server.domain.coupon.CouponEvent
import kr.hhplus.be.server.domain.coupon.CouponEventPublisher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class CouponSpringEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val kafkaTemplate: KafkaTemplate<String, CouponEvent.Created>
) : CouponEventPublisher {

    override fun publish(event: CouponEvent.Completed) {
        applicationEventPublisher.publishEvent(event)
    }

    override fun publish(event: CouponEvent.Created) {
        kafkaTemplate.send("coupon.created", event.couponId.toString(), event)
    }

}