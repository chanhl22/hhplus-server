package kr.hhplus.be.server.interfaces.api.coupon

import kr.hhplus.be.server.application.coupon.CouponResult.Issue
import kr.hhplus.be.server.domain.coupon.Coupon
import java.time.LocalDateTime

class CouponResponse {
    data class FirstComeIssue(
        val couponId: Long,
        val name: String,
        val discountType: String,
        val discountValue: Int,
        val expiresAt: LocalDateTime,
    )

    data class IssueCouponFirstCome(
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

        fun of(coupon: Coupon): IssueCouponFirstCome {
            return IssueCouponFirstCome(
                couponId = coupon.id,
                name = coupon.name,
                discountType = coupon.discountType.description,
                discountValue = coupon.discountValue,
                expiresAt = coupon.expiredAt
            )
        }
    }

}