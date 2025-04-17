package kr.hhplus.be.server.fixture.coupon

import kr.hhplus.be.server.application.coupon.CouponCriteria.Issue

object CouponCriterionFixture {
    fun create(
        userId: Long = 1L,
        couponId: Long = 1L
    ): Issue {
        return Issue(
            userId = userId,
            couponId = couponId,
        )
    }

}