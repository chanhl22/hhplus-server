package kr.hhplus.be.server.fixture.coupon

import kr.hhplus.be.server.application.coupon.CouponResult.Issue
import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.coupon.DiscountType
import java.time.LocalDateTime

object CouponResultFixture {
    fun create(
        couponId: Long = 1L,
        name: String = "1000원 할인 쿠폰",
        discountType: DiscountType = DiscountType.AMOUNT,
        discountValue: Int = 1000,
        remainingQuantity: Int = 20,
        expiresAt: LocalDateTime = LocalDateTime.now().plusMonths(1)
    ): Issue {
        val coupon = Coupon(
            id = couponId,
            name = name,
            discountType = discountType,
            discountValue = discountValue,
            remainingQuantity = remainingQuantity,
            expiredAt = expiresAt
        )

        return Issue(
            coupon = coupon
        )
    }
}