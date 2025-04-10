package kr.hhplus.be.server.application.coupon

import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.user.UserService
import org.springframework.stereotype.Component
import kr.hhplus.be.server.application.coupon.CouponCriteria.CouponCriterion
import kr.hhplus.be.server.application.coupon.CouponResults.CouponResult

@Component
class CouponFacade(
    private val couponService: CouponService,
    private val userService: UserService
) {

    fun issueCouponFirstCome(criterion: CouponCriterion): CouponResult {
        val user = userService.find(criterion.userId)

        val coupon = couponService.issueCoupon(criterion.toCommand(user))
        return CouponResult.of(coupon)
    }

}