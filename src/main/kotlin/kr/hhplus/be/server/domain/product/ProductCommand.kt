package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.order.OrderedProducts

class ProductCommand {
    data class Deduct(
        val orderId: Long,
        val userId: Long,
        val pointId: Long,
        val products: List<Pair<Long, Int>>,
        val couponId: Long?
    ) {
        fun getProductIds(): List<Long> {
            return products.map { it.first }
        }

        fun createOrderProductQuantityCountMap(): Map<Long, Int> {
            return products.associate { it.first to it.second }
        }

        fun toEvent(totalPrice: Int, orderedProducts: OrderedProducts): ProductEvent.Completed {
            return ProductEvent.Completed(
                orderId = orderId,
                userId = userId,
                pointId = pointId,
                products = products,
                couponId = couponId,
                totalPrice = totalPrice,
                productsDetail = orderedProducts.products.map {
                    ProductEvent.OrderedProduct(
                        productId = it.productId,
                        name = it.name,
                        price = it.price,
                        quantity = it.quantity
                    )
                }
            )
        }
    }

    data class OrderProduct(
        val productId: Long,
        val quantity: Int
    )

    data class ProductStatistics(
        val productId: Long,
        val totalSales: Int
    )

    companion object {
        fun of(productId: Long, totalSales: Int): ProductStatistics {
            return ProductStatistics(
                productId = productId,
                totalSales = totalSales
            )
        }
    }

}