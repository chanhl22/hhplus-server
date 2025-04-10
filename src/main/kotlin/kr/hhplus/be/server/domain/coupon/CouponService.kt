package kr.hhplus.be.server.domain.coupon

import org.springframework.stereotype.Service

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository
) {
    fun find(couponId: Long, userId: Long): Coupon? {
        return userCouponRepository.findByCouponIdAndUserId(couponId, userId)
            .firstOrNull()
            ?.coupon
    }

}