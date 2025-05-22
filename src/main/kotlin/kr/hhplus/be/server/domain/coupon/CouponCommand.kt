package kr.hhplus.be.server.domain.coupon

class CouponCommand {
    data class Use(
        val orderId: Long,
        val userId: Long,
        val pointId: Long,
        val products: List<Pair<Long, Int>>,
        val couponId: Long?,
        val totalPrice: Int,
        val productsDetail: List<OrderedProduct>
    ) {
        fun toEvent(
            totalPrice: Int,
            coupon: Coupon?
        ): CouponEvent.Completed {
            return CouponEvent.Completed(
                orderId = orderId,
                userId = userId,
                pointId = pointId,
                products = products,
                couponId = couponId,
                totalPrice = totalPrice,
                productsDetail = productsDetail.map {
                    CouponEvent.OrderedProduct(
                        productId = it.productId,
                        name = it.name,
                        price = it.price,
                        quantity = it.quantity
                    )
                },
                discountType = coupon?.discountType?.description,
                discountValue = coupon?.discountValue
            )
        }

        fun toEvent(): CouponEvent.Completed {
            return CouponEvent.Completed(
                orderId = orderId,
                userId = userId,
                pointId = pointId,
                products = products,
                couponId = couponId,
                totalPrice = totalPrice,
                productsDetail = productsDetail.map {
                    CouponEvent.OrderedProduct(
                        productId = it.productId,
                        name = it.name,
                        price = it.price,
                        quantity = it.quantity
                    )
                },
                discountType = null,
                discountValue = null
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