package kr.hhplus.be.server.domain.coupon

interface CouponRepository {

    fun find(couponId: Long): Coupon

    fun findWithPessimisticLock(couponId: Long): Coupon

    fun save(coupon: Coupon): Coupon

    fun reserveFirstCome(couponId: Long, userId: Long): CouponReserveStatus

    fun alreadyIssue(couponId: Long, userId: Long): Boolean

    fun existsQuantityKey(couponId: Long): Boolean

    fun registerQuantityKey(couponId: Long, remainingQuantity: Int)

    fun findActiveCoupon(): Set<String>

    fun updateSuccess(couponId: String): List<String>

}