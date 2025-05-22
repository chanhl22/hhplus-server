package kr.hhplus.be.server.domain.payment

class PaymentCommand {
    data class Save(
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
        fun toEvent(): PaymentEvent.Completed {
            return PaymentEvent.Completed(
                orderId = orderId,
                userId = userId,
                pointId = pointId,
                products = products,
                couponId = couponId,
                totalPrice = totalPrice,
                productsDetail = productsDetail.map {
                    PaymentEvent.OrderedProduct(
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