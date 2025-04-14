package kr.hhplus.be.server.application.coupon

import kr.hhplus.be.server.domain.coupon.Coupon

@Suppress("unused")
class CouponResults {
    data class CouponResult (
        val coupon: Coupon
    ) {
        companion object {
            fun of(coupon: Coupon): CouponResult {
                return CouponResult(
                    coupon = coupon
                )
            }
        }
    }
}
