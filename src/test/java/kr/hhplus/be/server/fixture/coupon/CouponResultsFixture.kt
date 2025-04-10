package kr.hhplus.be.server.fixture.coupon

import kr.hhplus.be.server.application.coupon.CouponResults.CouponResult
import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.coupon.DiscountType
import kr.hhplus.be.server.domain.point.Point
import kr.hhplus.be.server.domain.user.User
import java.time.LocalDateTime

object CouponResultFixture {
    fun create(
        userId: Long = 1L,
        username: String = "이찬희B",
        pointId: Long = 1L,
        balance: Int = 100000,
        couponId: Long = 1L,
        name: String = "1000원 할인 쿠폰",
        discountType: DiscountType = DiscountType.AMOUNT,
        discountValue: Int = 1000,
        remainingQuantity: Int = 20,
        expiresAt: LocalDateTime = LocalDateTime.now().plusMonths(1)
    ): CouponResult {
        val coupon = Coupon(
            id = couponId,
            user = User(
                id = userId,
                name = username,
                point = Point(
                    id = pointId,
                    balance = balance
                )
            ),
            name = name,
            discountType = discountType,
            discountValue = discountValue,
            remainingQuantity = remainingQuantity,
            expiresAt = expiresAt
        )

        return CouponResult(
            coupon = coupon
        )
    }
}