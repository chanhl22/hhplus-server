package kr.hhplus.be.server.application.coupon

class CouponCriteria {
    data class Issue(
        val userId: Long,
        val couponId: Long
    )

    companion object {
        fun of(userId: Long, couponId: Long): Issue {
            return Issue(userId, couponId)
        }
    }

}