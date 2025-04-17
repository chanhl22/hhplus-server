package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.order.coupon.OrderCoupon

class OrderCommand {
    data class Order(
        val orderPoint: OrderPoint,
        val orderedProducts: OrderedProducts,
        val orderCoupon: OrderCoupon
    )

    companion object {
        fun of(orderPoint: OrderPoint, orderedProducts: OrderedProducts, orderCoupon: OrderCoupon): Order {
            return Order(
                orderPoint = orderPoint,
                orderedProducts = orderedProducts,
                orderCoupon = orderCoupon
            )
        }
    }

}