package kr.hhplus.be.server.interfaces.coupon

import kr.hhplus.be.server.application.coupon.CouponResults.CouponResult
import java.time.LocalDateTime

@Suppress("unused")
class CouponResponses {
    data class FirstComeCouponResponse(
        val couponId: Long,
        val name: String,
        val discountType: String,
        val discountValue: Int,
        val expiresAt: LocalDateTime,
    ) {
        companion object {
            fun of(result: CouponResult): FirstComeCouponResponse {
                return FirstComeCouponResponse(
                    couponId = result.coupon.id!!,
                    name = result.coupon.name,
                    discountType = result.coupon.name,
                    discountValue = result.coupon.discountValue,
                    expiresAt = result.coupon.expiredAt
                )
            }
        }
    }

}