package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.user.User

class UserCoupon(
    val id: Long? = null,
    val user: User,
    var coupon: Coupon? = null
) {
    companion object {
        fun create(
            user: User,
            coupon: Coupon
        ): UserCoupon {
            val userCoupon = UserCoupon(user = user)
            userCoupon.changeCoupon(coupon)
            return userCoupon
        }
    }

    fun changeCoupon(coupon: Coupon) {
        this.coupon = coupon
        coupon.userCoupons.add(this)
    }

}
