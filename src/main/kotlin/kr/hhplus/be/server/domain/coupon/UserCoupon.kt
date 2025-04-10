package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.user.User

class UserCoupon(
    val id: Long = 0L,
    val user: User,
    val coupon: Coupon
) {
    companion object {
        fun create(
            user: User,
            coupon: Coupon
        ): UserCoupon {
            return UserCoupon(
                user = user,
                coupon = coupon,
            )
        }
    }

}
