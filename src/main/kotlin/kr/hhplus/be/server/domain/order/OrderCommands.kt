package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.user.User

class OrderCommands {
    data class OrderCommand(
        val user: User,
        val products: List<Product>,
        val coupon: Coupon?
    ) {
        companion object {
            fun of(user: User, products: List<Product>, coupon: Coupon?): OrderCommand {
                return OrderCommand(
                    user = user,
                    products = products,
                    coupon = coupon
                )
            }
        }
    }

}