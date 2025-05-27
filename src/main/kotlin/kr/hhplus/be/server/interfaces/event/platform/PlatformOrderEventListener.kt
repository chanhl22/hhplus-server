package kr.hhplus.be.server.interfaces.event.platform

import kr.hhplus.be.server.domain.order.OrderEvent
import kr.hhplus.be.server.domain.platform.PlatformSendService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class PlatformOrderEventListener(
    private val platformSendService: PlatformSendService
) {

    @KafkaListener(topics = ["order_completed"], groupId = "group-1")
    fun handle(event: OrderEvent.Completed) {
        val command = PlatformOrderEventMapper.toCommand(event)
        platformSendService.send(command)
    }

}