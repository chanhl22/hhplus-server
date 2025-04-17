package kr.hhplus.be.server.application.coupon

import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.user.UserService
import org.springframework.stereotype.Service

@Service
class CouponFacade(
    private val couponService: CouponService,
    private val userService: UserService
) {

    fun issueCouponFirstCome(criteria: CouponCriteria.Issue): CouponResult.Issue {
        val user = userService.find(criteria.userId)

        val coupon = couponService.issueCoupon(criteria.couponId, user.id)
        return CouponResult.of(coupon)
    }

}