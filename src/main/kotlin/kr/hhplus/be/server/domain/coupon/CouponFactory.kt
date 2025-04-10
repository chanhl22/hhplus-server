package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.user.User
import org.springframework.stereotype.Component

@Component
class CouponFactory {
    fun create(user: User, coupon: Coupon): UserCoupon {
        return UserCoupon.create(user, coupon)
    }
}