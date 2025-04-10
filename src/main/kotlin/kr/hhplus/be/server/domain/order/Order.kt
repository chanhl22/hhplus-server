package kr.hhplus.be.server.domain.order

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
    }

    private fun initOrderProducts(products: List<Product>) {
        this.orderProducts.addAll(
            products.map { product ->
                OrderProduct(order = this, product = product)
            }
        )
    }
}
