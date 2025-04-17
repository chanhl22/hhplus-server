package kr.hhplus.be.server.fixture.coupon

import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.coupon.DiscountType
import kr.hhplus.be.server.domain.coupon.UserCoupon
import java.time.LocalDateTime

object CouponDomainFixture {
    fun create(
        couponId: Long = 1L,
        name: String = "1000원 할인 쿠폰",
        discountType: DiscountType = DiscountType.AMOUNT,
        discountValue: Int = 1000,
        remainingQuantity: Int = 20,
        expiredAt: LocalDateTime = LocalDateTime.now().plusMonths(1)
    ): Coupon {
        return Coupon(
            id = couponId,
            name = name,
            discountType = discountType,
            discountValue = discountValue,
            remainingQuantity = remainingQuantity,
            expiredAt = expiredAt
        )
    }
}

object UserCouponDomainFixture {
    fun create(
        userCouponId: Long = 1,
        userId: Long = 1L,
        couponId: Long? = null,
        isUsed: Boolean = false
    ): UserCoupon {
        return UserCoupon(
            id = userCouponId,
            userId = userId,
            couponId = couponId,
            isUsed = isUsed
        )
    }
}