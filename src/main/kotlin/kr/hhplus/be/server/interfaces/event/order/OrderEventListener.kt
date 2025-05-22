package kr.hhplus.be.server.interfaces.event.order

import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.payment.PaymentEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class OrderEventListener(
    private val orderService: OrderService
) {

    @EventListener
    fun handle(event: PaymentEvent.Completed) {
        println("🔥 이벤트 수신됨: $event")
        val command = OrderEventMapper.toCommand(event)
        orderService.complete(command)
    }

}