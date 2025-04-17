package kr.hhplus.be.server.interfaces.coupon

import kr.hhplus.be.server.application.coupon.CouponResult.Issue
import java.time.LocalDateTime

class CouponResponse {
    data class FirstComeIssue(
        val couponId: Long,
        val name: String,
        val discountType: String,
        val discountValue: Int,
        val expiresAt: LocalDateTime,
    )

    companion object {
        fun of(result: Issue): FirstComeIssue {
            return FirstComeIssue(
                couponId = result.coupon.id,
                name = result.coupon.name,
                discountType = result.coupon.name,
                discountValue = result.coupon.discountValue,
                expiresAt = result.coupon.expiredAt
            )
        }
    }

}