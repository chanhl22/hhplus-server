package kr.hhplus.be.server.domain.coupon

import org.springframework.stereotype.Service
import kr.hhplus.be.server.domain.coupon.CouponCommands.IssueCouponCommand

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository,
    private val couponFactory: CouponFactory
) {
    fun find(couponId: Long, userId: Long): Coupon? {
        return userCouponRepository.findByCouponIdAndUserId(couponId, userId)
            .firstOrNull()
            ?.coupon
    }

    fun issueCoupon(command: IssueCouponCommand): Coupon {
        val coupon = couponRepository.find(command.couponId)

        if (coupon.isLessThanZero()) {
            throw IllegalStateException("쿠폰이 모두 소진되었습니다.")
        }

        coupon.remainingQuantity -= 1
        couponRepository.save(coupon)
        userCouponRepository.save(couponFactory.create(command.user, coupon))
        return coupon
    }

}