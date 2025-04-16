package kr.hhplus.be.server.domain.order.coupon

object EmptyOrderCoupon : OrderCoupon {

    override fun apply(totalPrice: Int): Int {
        return totalPrice
    }

    override fun getCouponId(): Long? {
        return null
    }

}