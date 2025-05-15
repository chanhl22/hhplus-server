package kr.hhplus.be.server.interfaces.coupon

class CouponRequest {
    data class FirstCome(
        val userId: Long,
        val couponId: Long
    )
}