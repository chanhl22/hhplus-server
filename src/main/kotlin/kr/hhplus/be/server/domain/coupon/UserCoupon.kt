package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.user.User

class UserCoupon(
    val id: Long? = null,
    val userId: Long,
    var couponId: Long? = null,
    val isUsed: Boolean = false
) {
    companion object {
        fun create(
            user: User,
            coupon: Coupon
        ): UserCoupon {
            val userCoupon = UserCoupon(userId = user.id, couponId = coupon.id)
//            userCoupon.changeCoupon(coupon)
            return userCoupon
        }
    }

//    fun changeCoupon(coupon: Coupon) {
//        this.coupon = coupon
//        coupon.userCoupons.add(this)
//    }

    fun used(): UserCoupon {
        return UserCoupon(id, userId, couponId, true)
    }

}
