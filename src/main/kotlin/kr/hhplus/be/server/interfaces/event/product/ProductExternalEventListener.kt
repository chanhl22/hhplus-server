package kr.hhplus.be.server.interfaces.event.product

import kr.hhplus.be.server.domain.order.OrderEvent
import kr.hhplus.be.server.domain.product.ProductService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ProductExternalEventListener(
    private val productService: ProductService
) {

    @EventListener
    fun handle(event: OrderEvent.Create) {
        println("🔥 이벤트 수신됨: $event")
        val command = ProductExternalEventMapper.toCommand(event)
        productService.deduct(command)
    }

}