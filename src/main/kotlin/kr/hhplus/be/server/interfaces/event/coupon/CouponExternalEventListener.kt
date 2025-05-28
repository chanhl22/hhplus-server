package kr.hhplus.be.server.interfaces.event.coupon

import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.product.ProductEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class CouponExternalEventListener(
    private val couponService: CouponService
) {

    @EventListener
    fun handle(event: ProductEvent.Completed) {
        println("🔥 이벤트 수신됨: $event")
        val command = CouponExternalEventMapper.toCommand(event)
        couponService.isUsed(command)
    }

}