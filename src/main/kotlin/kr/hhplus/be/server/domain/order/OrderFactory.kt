package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.user.User
import org.springframework.stereotype.Component

@Component
class OrderFactory {
    fun create(user: User, products: List<Product>): Order {
        return Order.create(user, products)
    }
}