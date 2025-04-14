package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.user.User
import org.springframework.stereotype.Component

@Component
class OrderFactory {
    fun create(user: User, products: List<Product>, coupon: Coupon?): Order {
        if (coupon == null) {
            return Order.create(user, products)
        }

        return Order.create(user, products, coupon)
    }
}