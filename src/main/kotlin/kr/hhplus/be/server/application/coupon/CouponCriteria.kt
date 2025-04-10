package kr.hhplus.be.server.application.coupon

import kr.hhplus.be.server.domain.coupon.CouponCommands.IssueCouponCommand
import kr.hhplus.be.server.domain.user.User

@Suppress("unused")
class CouponCriteria {
    data class CouponCriterion(
        val userId: Long,
        val couponId: Long
    ) {
        companion object {
            fun of(userId: Long, couponId: Long): CouponCriterion {
                return CouponCriterion(userId, couponId)
            }
        }

        fun toCommand(user: User): IssueCouponCommand {
            return IssueCouponCommand.of(user, couponId)
        }
    }
}