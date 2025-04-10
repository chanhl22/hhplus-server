package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.domain.product.ProductCommands.ProductCommand
import kr.hhplus.be.server.domain.product.ProductCommands.ProductsCommand

class OrderCriteria {
    data class OrderCriterion(
        val userId: Long,
        val products: List<OrderProductCriterion>
    ) {
        companion object {
            fun of(userId: Long, products: List<OrderProductCriterion>): OrderCriterion {
                return OrderCriterion(userId, products)
            }
        }

        fun toProductCommand(): ProductsCommand {
            return ProductsCommand.of(
                products = products.map { product ->
                    ProductCommand(
                        productId = product.productId,
                        quantity = product.quantity,
                    )
                }
            )
        }
    }

    data class OrderProductCriterion(
        val productId: Long,
        val quantity: Int
    )
}