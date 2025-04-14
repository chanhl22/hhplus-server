package kr.hhplus.be.server.interfaces.coupon.response

import java.time.LocalDateTime

data class CouponsResponse(
    val couponId: Long,
    val name: String,
    val discountType: String,
    val discountValue: Int,
    val expiresAt: LocalDateTime,
    val issuedAt: LocalDateTime,
    val isUsed: Boolean
)