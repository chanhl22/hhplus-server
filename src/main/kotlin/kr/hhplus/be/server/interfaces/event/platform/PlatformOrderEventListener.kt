package kr.hhplus.be.server.interfaces.event.platform

import kr.hhplus.be.server.domain.order.OrderEvent
import kr.hhplus.be.server.domain.platform.PlatformSendService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class PlatformOrderEventListener(
    private val platformSendService: PlatformSendService
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(event: OrderEvent.Completed) {
        val command = PlatformOrderEventMapper.toCommand(event)
        platformSendService.send(command)
    }

}