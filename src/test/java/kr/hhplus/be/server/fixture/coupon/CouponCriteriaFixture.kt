package kr.hhplus.be.server.fixture.coupon

import kr.hhplus.be.server.application.coupon.CouponCriteria.CouponCriterion

object CouponCriterionFixture {
    fun create(
        userId: Long = 1L,
        couponId: Long = 1L
    ): CouponCriterion {
        return CouponCriterion(
            userId = userId,
            couponId = couponId,
        )
    }

}