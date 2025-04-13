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
        val coupon = couponRepository.find(command.couponId).publish()
        coupon.deduct()
        couponRepository.save(coupon)
        userCouponRepository.save(couponFactory.create(command.user, coupon))
        return coupon
    }

}