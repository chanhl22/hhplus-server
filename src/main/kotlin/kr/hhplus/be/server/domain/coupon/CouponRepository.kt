package kr.hhplus.be.server.domain.coupon

interface CouponRepository {

    fun find(couponId: Long): Coupon

    fun findWithPessimisticLock(couponId: Long): Coupon

    fun save(coupon: Coupon): Coupon

}