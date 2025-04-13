package kr.hhplus.be.server.fixture.order

import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.order.OrderProduct
import kr.hhplus.be.server.domain.user.User
import kr.hhplus.be.server.fixture.user.UserFixture

object OrderDomainsFixture {
    fun create(
        orderId: Long = 1L,
        user: User = UserFixture.create(),
        totalPrice: Int = 100000,
        orderProducts: List<OrderProduct> = emptyList()
    ): Order {
        val order = Order(
            id = orderId,
            user = user,
            totalPrice = totalPrice
        )
        orderProducts.map {orderProduct -> order.orderProducts.add(orderProduct) }
        return order
    }

}