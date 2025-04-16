package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.order.coupon.OrderCoupon
import java.time.LocalDateTime

class Order(
    val id: Long? = null,
    val userId: Long,
    val totalPrice: Int,
    val couponId: Long?,
    val registeredAt: LocalDateTime = LocalDateTime.now(),
    val orderProducts: MutableList<OrderProduct> = mutableListOf()
) {
    companion object {
        fun create(
            orderPoint: OrderPoint,
            orderedProducts: OrderedProducts,
            orderCoupon: OrderCoupon
        ): Order {
            orderedProducts.isEmptyOrder()
            orderedProducts.isEnoughQuantity()

            val totalPrice = orderedProducts.calculateTotalPrice()
            val discountedTotalPrice = orderCoupon.apply(totalPrice)
            orderPoint.isEnoughBalance(discountedTotalPrice)

            val order = Order(
                userId = orderPoint.userId,
                totalPrice = discountedTotalPrice,
                couponId = orderCoupon.getCouponId()
            )

            val productIds = orderedProducts.getProductIds()
            order.initOrderProducts(productIds)
            return order
        }
    }

    private fun initOrderProducts(productIds: List<Long>) {
        this.orderProducts.addAll(
            productIds.map { productId ->
                OrderProduct(order = this, productId = productId)
            }
        )
    }

}
