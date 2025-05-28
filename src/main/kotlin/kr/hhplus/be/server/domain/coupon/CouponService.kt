package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.order.coupon.OrderCoupon
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CouponService(
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository,
    private val couponEventPublisher: CouponEventPublisher
) {

    @Transactional
    fun reserveFirstCome(couponId: Long, userId: Long) {
        if (isEmptyQuantityKey(couponId)) {
            val coupon = couponRepository.find(couponId)
            couponRepository.registerQuantityKey(couponId, coupon.quantity)
        }

        val status = couponRepository.reserveFirstCome(couponId, userId)
        when (status) {
            CouponReserveStatus.SUCCESS -> {
                couponEventPublisher.publish(CouponEvent.Created(couponId, userId))
                return
            }
            CouponReserveStatus.ALREADY_REQUESTED -> throw IllegalStateException("이미 요청한 유저입니다.")
            CouponReserveStatus.OUT_OF_STOCK -> throw IllegalStateException("쿠폰 수량이 부족합니다.")
            CouponReserveStatus.NO_STOCK_INFO -> throw IllegalStateException("쿠폰의 정보가 존재하지 않습니다.")
        }
    }

    private fun isEmptyQuantityKey(couponId: Long): Boolean {
        return !couponRepository.existsQuantityKey(couponId)
    }

    @Transactional
    fun issueCoupon() {
        val activeCoupons = couponRepository.findActiveCoupon()
        for (couponId in activeCoupons) {
            val successUsers = couponRepository.updateSuccess(couponId)
            val userCoupons = successUsers.map { userId ->
                UserCoupon.create(userId = userId.toLong(), couponId = couponId.toLong())
            }
            userCouponRepository.saveAll(userCoupons)
        }
    }

    @Transactional
    fun issueCoupon(events: List<CouponEvent.Created>) {
        val userCoupons = events.map { event ->
            UserCoupon.create(userId = event.userId, couponId = event.couponId)
        }
        userCouponRepository.saveAll(userCoupons)
    }

    @Transactional
    fun isUsed(command: CouponCommand.Use) {
        if (isEmptyCoupon(command)) {
            couponEventPublisher.publish(command.toEvent())
            return
        }

        val couponId = command.couponId!!
        val coupon = couponRepository.find(couponId)
        val orderCoupon = OrderCoupon.from(coupon)
        val discountedTotalPrice = orderCoupon.apply(command.totalPrice)

        val userCoupon =
            userCouponRepository.findByCouponIdAndUserIdAndIsUsed(couponId, command.userId, false)
                .firstOrNull()
        if (userCoupon == null) {
            throw IllegalStateException("해당 쿠폰을 사용할 수 없습니다. 쿠폰이 존재하지 않거나 이미 사용되었습니다.")
        }

        val usedUserCoupon = userCoupon.used()
        userCouponRepository.save(usedUserCoupon)

        couponEventPublisher.publish(command.toEvent(discountedTotalPrice, coupon))
    }

    private fun isEmptyCoupon(command: CouponCommand.Use): Boolean {
        if (command.couponId == null) {
            return true
        }

        if (!userCouponRepository.existsByCouponIdAndUserIdAndIsUsed(command.couponId, command.userId, false)) {
            return true
        }

        return false
    }


    fun polling(couponId: Long, userId: Long): String {
        val userCoupon = userCouponRepository.findByCouponIdAndUserIdAndIsUsed(couponId, userId, false)
            .firstOrNull()

        if (userCoupon == null) {
            return "FAIL"
        }
        return "SUCCESS"
    }

}