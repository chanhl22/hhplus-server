package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.coupon.CouponCommands.IssueCouponCommand
import org.springframework.stereotype.Service

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository
) {
//    fun find(couponId: Long?, userId: Long): Coupon? {
//        return userCouponRepository.findByCouponIdAndUserId(couponId, userId)
//            .firstOrNull()
//            ?.coupon
//    }

    fun find(couponId: Long?, userId: Long): Coupon? {
        if (couponId != null && userCouponRepository.existsByCouponIdAndUserIdAndIsUsed(couponId, userId, false)) {
            return couponRepository.find(couponId)
        }

        return null
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

    fun isUsed(couponId: Long?, userId: Long) {
        if (couponId == null) {
            return
        }

        val userCoupon = userCouponRepository.findByCouponIdAndUserIdAndIsUsed(couponId, userId, false)
            .firstOrNull()

        if (userCoupon == null) {
            throw IllegalStateException("해당 쿠폰을 사용할 수 없습니다. 쿠폰이 존재하지 않거나 이미 사용되었습니다.")
        }

        val usedUserCoupon = userCoupon.used()
        userCouponRepository.save(usedUserCoupon)
    }

}