package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.user.User

class OrderCommands {
    data class OrderCommand(
        val user: User,
        val products: List<Product>,
    ) {
        companion object {
            fun of(user: User, products: List<Product>): OrderCommand {
                return OrderCommand(
                    user = user,
                    products = products
                )
            }
        }
    }

}