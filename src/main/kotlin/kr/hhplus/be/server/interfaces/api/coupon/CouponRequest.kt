package kr.hhplus.be.server.interfaces.api.coupon

class CouponRequest {
    data class FirstCome(
        val userId: Long,
        val couponId: Long
    )
}