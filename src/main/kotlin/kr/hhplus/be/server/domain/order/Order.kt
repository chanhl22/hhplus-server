package kr.hhplus.be.server.domain.order

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import kr.hhplus.be.server.domain.order.coupon.OrderCoupon
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class Order(
    val userId: Long,
    val totalPrice: Int,
    val couponId: Long?,
    val registeredAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "order")
    val orderProducts: MutableList<OrderProduct> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
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
