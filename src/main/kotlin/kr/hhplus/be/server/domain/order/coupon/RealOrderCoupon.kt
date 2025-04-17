package kr.hhplus.be.server.domain.order.coupon

import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.coupon.DiscountType
import java.time.LocalDateTime

class RealOrderCoupon(
    val id: Long,
    val name: String,
    val discountType: DiscountType,
    val discountValue: Int,
    val remainingQuantity: Int,
    val expiredAt: LocalDateTime
) : OrderCoupon {

    override fun apply(totalPrice: Int): Int {
        val discountedPrice = when (discountType) {
            DiscountType.PERCENT -> {
                val discount = (totalPrice * discountValue) / 100
                totalPrice - discount
            }

            DiscountType.AMOUNT -> {
                totalPrice - discountValue
            }
        }

        return discountedPrice.coerceAtLeast(0)
    }

    override fun getCouponId(): Long {
        return id
    }

    companion object {
        fun create(coupon: Coupon): RealOrderCoupon {
            return RealOrderCoupon(
                id = coupon.id,
                name = coupon.name,
                discountType = coupon.discountType,
                discountValue = coupon.discountValue,
                remainingQuantity = coupon.remainingQuantity,
                expiredAt = coupon.expiredAt
            )
        }
    }

}