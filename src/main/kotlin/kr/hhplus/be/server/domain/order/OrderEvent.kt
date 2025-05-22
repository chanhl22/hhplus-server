package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.user.User

class OrderEvent {
    data class Completed(
        val orderId: Long,
        val totalPrice: Int,
        val products: List<OrderedProduct>,
        val userId: Long,
        val name: String,
        val couponId: Long?,
        val discountType: String?,
        val discountValue: Int?,
    )

    data class OrderedProduct(
        val productId: Long,
        val name: String,
        val price: Int,
        val quantity: Int
    )

    companion object {
        fun of(order: Order, orderedProducts: OrderedProducts, user: User, coupon: Coupon?): Completed {
            return Completed(
                orderId = order.id,
                totalPrice = order.totalPrice,
                products = orderedProducts.products.map {
                    OrderedProduct(
                        productId = it.productId,
                        name = it.name,
                        price = it.price,
                        quantity = it.quantity
                    )
                },
                userId = user.id,
                name = user.name,
                couponId = coupon?.id,
                discountType = coupon?.discountType?.description,
                discountValue = coupon?.discountValue
            )
        }
    }

}