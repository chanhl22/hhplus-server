package kr.hhplus.be.server.domain.coupon

interface UserCouponRepository {

    fun findByCouponIdAndUserIdAndIsUsed(couponId: Long, userId: Long, isUsed: Boolean): List<UserCoupon>

    fun save(userCoupon: UserCoupon): UserCoupon

    fun existsByCouponIdAndUserIdAndIsUsed(couponId: Long, userId: Long, isUsed: Boolean): Boolean

    fun saveAll(userCoupons: List<UserCoupon>)

}