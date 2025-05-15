package kr.hhplus.be.server.infrastructure.coupon

import kr.hhplus.be.server.domain.coupon.UserCoupon
import kr.hhplus.be.server.domain.coupon.UserCouponRepository
import org.springframework.stereotype.Repository

@Repository
class UserCouponRepositoryImpl(
    private val userCouponJpaRepository: UserCouponJpaRepository
) : UserCouponRepository {
    override fun findByCouponIdAndUserIdAndIsUsed(couponId: Long, userId: Long, isUsed: Boolean): List<UserCoupon> {
        return userCouponJpaRepository.findByCouponIdAndUserIdAndIsUsed(couponId, userId, isUsed)
    }

    override fun save(userCoupon: UserCoupon): UserCoupon {
        return userCouponJpaRepository.save(userCoupon)
    }

    override fun existsByCouponIdAndUserIdAndIsUsed(couponId: Long, userId: Long, isUsed: Boolean): Boolean {
        return userCouponJpaRepository.existsByCouponIdAndUserIdAndIsUsed(couponId, userId, isUsed)
    }

    override fun saveAll(userCoupons: List<UserCoupon>) {
        userCouponJpaRepository.saveAll(userCoupons)
    }

}