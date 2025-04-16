package kr.hhplus.be.server.domain.coupon

import org.springframework.stereotype.Repository

@Repository
interface UserCouponRepository {

    fun findByCouponIdAndUserIdAndIsUsed(couponId: Long, userId: Long, isUsed: Boolean): List<UserCoupon>

    fun save(userCoupon: UserCoupon): UserCoupon

    fun existsByCouponIdAndUserIdAndIsUsed(couponId: Long, userId: Long, isUsed: Boolean): Boolean

}