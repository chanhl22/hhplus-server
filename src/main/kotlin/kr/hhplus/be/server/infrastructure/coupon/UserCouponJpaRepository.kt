package kr.hhplus.be.server.infrastructure.coupon

import kr.hhplus.be.server.domain.coupon.UserCoupon
import org.springframework.data.jpa.repository.JpaRepository

interface UserCouponJpaRepository : JpaRepository<UserCoupon, Long> {

    fun findByCouponIdAndUserIdAndIsUsed(couponId: Long, userId: Long, isUsed: Boolean): List<UserCoupon>

    fun  existsByCouponIdAndUserIdAndIsUsed(couponId: Long, userId: Long, isUsed: Boolean): Boolean
}