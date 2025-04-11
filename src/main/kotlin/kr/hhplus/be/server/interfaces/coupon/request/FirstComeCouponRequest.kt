package kr.hhplus.be.server.interfaces.coupon.request

data class FirstComeCouponRequest(
    val userId: Long,
    val couponId: Long
)