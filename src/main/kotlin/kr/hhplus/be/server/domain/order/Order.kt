package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.coupon.DiscountType
import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.user.User
import java.time.LocalDateTime

class Order(
    val id: Long = 0L,
    val user: User,
    val totalPrice: Int,
    val registeredDateTime: LocalDateTime = LocalDateTime.now(),
    val orderProducts: MutableList<OrderProduct> = mutableListOf()
) {
    companion object {
        fun create(
            user: User,
            products: List<Product>
        ): Order {
            val order = Order(
                user = user,
                totalPrice = products.sumOf { it.price }
            )

            order.initOrderProducts(products)
            return order
        }

        fun create(
            user: User,
            products: List<Product>,
            coupon: Coupon
        ): Order {
            val originalTotalPrice = products.sumOf { it.price }
            val discountedTotalPrice = discountByCoupon(coupon, originalTotalPrice)

            val order = Order(
                user = user,
                totalPrice = discountedTotalPrice
            )

            order.initOrderProducts(products)
            return order
        }

        private fun discountByCoupon(coupon: Coupon, originalTotalPrice: Int) =
            when (coupon.discountType) {
                DiscountType.PERCENT -> {
                    val discount = (originalTotalPrice * coupon.discountValue) / 100
                    originalTotalPrice - discount
                }

                DiscountType.AMOUNT -> {
                    originalTotalPrice - coupon.discountValue
                }
            }.coerceAtLeast(0)
    }

    private fun initOrderProducts(products: List<Product>) {
        this.orderProducts.addAll(
            products.map { product ->
                OrderProduct(order = this, product = product)
            }
        )
    }
}
