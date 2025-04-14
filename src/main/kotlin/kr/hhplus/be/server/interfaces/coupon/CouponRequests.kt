package kr.hhplus.be.server.interfaces.coupon

import kr.hhplus.be.server.application.coupon.CouponCriteria.CouponCriterion

@Suppress("unused")
class CouponRequests {
    data class FirstComeCouponRequest(
        val userId: Long,
        val couponId: Long
    ) {
        fun toCriterion(): CouponCriterion {
            return CouponCriterion.of(
                userId = userId,
                couponId = couponId
            )
        }
    }
}