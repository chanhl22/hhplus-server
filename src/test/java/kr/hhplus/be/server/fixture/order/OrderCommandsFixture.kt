package kr.hhplus.be.server.fixture.order

import kr.hhplus.be.server.domain.order.OrderCommands.OrderCommand
import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.user.User

object OrderCommandFixture {
    fun create(
        user: User,
        products: List<Product>
    ): OrderCommand {
        return OrderCommand(
            user = user,
            products = products
        )
    }
}