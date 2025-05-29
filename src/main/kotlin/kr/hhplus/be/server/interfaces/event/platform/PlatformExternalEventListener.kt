package kr.hhplus.be.server.interfaces.event.platform

import kr.hhplus.be.server.domain.order.OrderEvent
import kr.hhplus.be.server.domain.platform.PlatformSendService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class PlatformExternalEventListener(
    private val platformSendService: PlatformSendService
) {

    @KafkaListener(topics = ["order.completed"], groupId = "platform-service")
    fun handle(event: OrderEvent.Completed, ack: Acknowledgment) {
        val command = PlatformExternalEventMapper.toCommand(event)
        platformSendService.send(command)
        ack.acknowledge()
    }

}