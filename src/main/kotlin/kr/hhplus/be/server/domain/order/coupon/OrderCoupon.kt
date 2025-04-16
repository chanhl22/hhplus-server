package kr.hhplus.be.server.domain.order.coupon

import kr.hhplus.be.server.domain.coupon.Coupon

interface OrderCoupon {

    fun apply(totalPrice: Int): Int

    fun getCouponId(): Long?

    companion object {
        fun from(coupon: Coupon?): OrderCoupon {
            return if (coupon != null) {
                RealOrderCoupon.create(coupon)
            } else {
                EmptyOrderCoupon
            }
        }
    }

}