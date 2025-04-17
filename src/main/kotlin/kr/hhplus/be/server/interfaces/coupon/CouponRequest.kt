package kr.hhplus.be.server.interfaces.coupon

import kr.hhplus.be.server.application.coupon.CouponCriteria

class CouponRequest {
    data class FirstComeIssue(
        val userId: Long,
        val couponId: Long
    ) {
        fun toCriterion(): CouponCriteria.Issue {
            return CouponCriteria.of(
                userId = userId,
                couponId = couponId
            )
        }
    }
}