package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.user.User

@Suppress("unused")
class CouponCommands {
    data class IssueCouponCommand(
        val user: User,
        val couponId: Long
    ) {
        companion object {
            fun of(user: User, couponId: Long): IssueCouponCommand {
                return IssueCouponCommand(
                    user = user,
                    couponId = couponId
                )
            }
        }
    }

}