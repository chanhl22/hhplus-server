package kr.hhplus.be.server.fixture.coupon

import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.coupon.DiscountType
import kr.hhplus.be.server.domain.user.User
import java.time.LocalDateTime

object CouponDomainFixture {
    fun create(
        couponId: Long = 1L,
        user: User,
        name: String = "1000원 할인 쿠폰",
        discountType: DiscountType = DiscountType.AMOUNT,
        discountValue: Int = 1000,
        remainingQuantity: Int = 20,
        expiresAt: LocalDateTime = LocalDateTime.now().plusMonths(1)
    ): Coupon {
        return Coupon(
            id = couponId,
            user = user,
            name = name,
            discountType = discountType,
            discountValue = discountValue,
            remainingQuantity = remainingQuantity,
            expiresAt = expiresAt
        )
    }

}