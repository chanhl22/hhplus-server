package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.domain.product.ProductCommands.ProductCommand
import kr.hhplus.be.server.domain.product.ProductCommands.ProductsCommand

class OrderCriteria {
    data class OrderCriterion(
        val userId: Long,
        val products: List<OrderProductCriterion>,
        val couponId: Long
    ) {
        companion object {
            fun of(userId: Long, products: List<OrderProductCriterion>, couponId: Long): OrderCriterion {
                return OrderCriterion(userId, products, couponId)
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