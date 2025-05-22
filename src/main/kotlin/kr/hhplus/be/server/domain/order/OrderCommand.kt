package kr.hhplus.be.server.domain.order

class OrderCommand {
    data class Create(
        val userId: Long,
        val pointId: Long,
        val products: List<OrderProduct>,
        val couponId: Long?
    ) {
        fun toEvent(orderId: Long): OrderEvent.Create {
            return OrderEvent.Create(
                orderId = orderId,
                userId = userId,
                pointId = pointId,
                products = getProductPairs(),
                couponId = couponId
            )
        }

        fun getProductPairs(): List<Pair<Long, Int>> {
            return products.map { it.productId to it.quantity }
        }
    }

    data class OrderProduct(
        val productId: Long,
        val quantity: Int
    )

    data class Completed(
        val orderId: Long,
        val userId: Long,
        val pointId: Long,
        val products: List<Pair<Long, Int>>,
        val couponId: Long?,
        val totalPrice: Int,
        val productsDetail: List<OrderedProduct>,
        val discountType: String?,
        val discountValue: Int?
    ) {
        fun getProductPairs(): List<Pair<Long, Int>> {
            return products.map { it.first to it.second }
        }

        fun toEvent(): OrderEvent.Completed {
            return OrderEvent.Completed(
                orderId = orderId,
                userId = userId,
                pointId = pointId,
                products = products,
                couponId = couponId,
                totalPrice = totalPrice,
                productsDetail = productsDetail.map {
                    OrderEvent.OrderedProduct(
                        productId = it.productId,
                        name = it.name,
                        price = it.price,
                        quantity = it.quantity
                    )
                },
                discountType = discountType,
                discountValue = discountValue
            )
        }
    }

    data class OrderedProduct(
        val productId: Long,
        val name: String,
        val price: Int,
        val quantity: Int
    )

}