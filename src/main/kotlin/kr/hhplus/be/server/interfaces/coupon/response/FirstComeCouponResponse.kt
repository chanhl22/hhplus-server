package kr.hhplus.be.server.interfaces.coupon.response

import java.time.LocalDateTime

data class FirstComeCouponResponse(
    val success: Boolean,
    val couponId: Long,
    val name: String,
    val discountType: String,
    val discountValue: Int,
    val expiresAt: LocalDateTime,
    val issuedAt: LocalDateTime,
)