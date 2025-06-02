package kr.hhplus.be.server.interfaces.event.point

import kr.hhplus.be.server.domain.coupon.CouponEvent
import kr.hhplus.be.server.domain.point.PointService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class PointExternalEventListener(
    private val pointService: PointService
) {

    @EventListener
    fun handle(event: CouponEvent.Completed) {
        val command = PointExternalEventMapper.toCommand(event)
        pointService.use(command)
    }

}