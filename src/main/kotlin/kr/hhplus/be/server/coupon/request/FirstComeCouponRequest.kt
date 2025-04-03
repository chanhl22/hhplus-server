package kr.hhplus.be.server.coupon.request

data class FirstComeCouponRequest(
    val userId: Long,
    val couponId: Long
)