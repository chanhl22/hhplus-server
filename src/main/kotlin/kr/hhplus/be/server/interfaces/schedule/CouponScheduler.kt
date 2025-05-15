package kr.hhplus.be.server.interfaces.schedule

import kr.hhplus.be.server.domain.coupon.CouponService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CouponScheduler(
    private val couponService: CouponService
) {

    @Scheduled(fixedDelay = 3000)
    fun runDailyAt4AMForStatistics() {
        couponService.issueCoupon()
    }

}