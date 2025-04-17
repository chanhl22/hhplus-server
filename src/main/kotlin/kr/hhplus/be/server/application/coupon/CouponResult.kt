package kr.hhplus.be.server.application.coupon

import kr.hhplus.be.server.domain.coupon.Coupon

class CouponResult {
    data class Issue(
        val coupon: Coupon
    )

    companion object {
        fun of(coupon: Coupon): Issue {
            return Issue(
                coupon = coupon
            )
        }
    }

}
