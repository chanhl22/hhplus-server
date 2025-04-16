//package kr.hhplus.be.server.fixture.coupon
//
//import kr.hhplus.be.server.domain.coupon.Coupon
//import kr.hhplus.be.server.domain.coupon.DiscountType
//import kr.hhplus.be.server.domain.coupon.UserCoupon
//import kr.hhplus.be.server.domain.user.User
//import kr.hhplus.be.server.fixture.user.UserFixture
//import java.time.LocalDateTime
//
//object CouponDomainFixture {
//    fun create(
//        couponId: Long = 1L,
//        userCoupons: List<UserCoupon> = emptyList(),
//        name: String = "1000원 할인 쿠폰",
//        discountType: DiscountType = DiscountType.AMOUNT,
//        discountValue: Int = 1000,
//        remainingQuantity: Int = 20,
//        expiresAt: LocalDateTime = LocalDateTime.now().plusMonths(1)
//    ): Coupon {
//        val coupon = Coupon(
//            id = couponId,
//            name = name,
//            discountType = discountType,
//            discountValue = discountValue,
//            remainingQuantity = remainingQuantity,
//            expiredAt = expiresAt
//        )
//        userCoupons.map { userCoupon -> coupon.userCoupons.add(userCoupon) }
//        return coupon
//    }
//
//    fun createUserCoupon(
//        userCouponId: Long = 1L,
//        user: User = UserFixture.create(),
//        coupon: Coupon = create()
//    ): UserCoupon {
//        val userCoupon = UserCoupon(
//            id = userCouponId,
//            user = user
//        )
//        userCoupon.changeCoupon(coupon)
//        return userCoupon
//    }
//
//}