package kr.hhplus.be.server.coupon.response

data class CouponResponse(
    val couponId: Long,
    val userId: Long,
    val discountAmount: Int
)