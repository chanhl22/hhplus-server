package kr.hhplus.be.server.domain.coupon

import org.springframework.stereotype.Repository

@Repository
interface UserCouponRepository {

    fun findByCouponIdAndUserId(couponId: Long, userId: Long): List<UserCoupon>

    fun save(userCoupon: UserCoupon): UserCoupon

}