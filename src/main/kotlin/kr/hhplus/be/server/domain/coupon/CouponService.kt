package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.coupon.CouponCommands.IssueCouponCommand
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

    fun issueCoupon(command: IssueCouponCommand): Coupon {
        val coupon = couponRepository.find(command.couponId)
            .publish()
            .deduct()
        val userCoupon = coupon.issueTo(command.user)

        val issuedCoupon = couponRepository.save(coupon)
        userCouponRepository.save(userCoupon)
        return issuedCoupon
    }

}