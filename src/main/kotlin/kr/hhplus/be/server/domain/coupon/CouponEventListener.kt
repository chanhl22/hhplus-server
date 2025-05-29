package kr.hhplus.be.server.domain.coupon

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class CouponEventListener(
    private val couponService: CouponService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["coupon.created"], groupId = "coupon-service")
    fun handle(events: List<ConsumerRecord<String, CouponEvent.Created>>, ack: Acknowledgment) {
        log.info("🔥 이벤트 수신됨: ${events.size}")
        val payloads = events.map { it.value() }
        couponService.issueCoupon(payloads)
        ack.acknowledge()
    }

}