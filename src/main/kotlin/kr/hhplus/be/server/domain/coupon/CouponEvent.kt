package kr.hhplus.be.server.domain.coupon

class CouponEvent {
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
    )

    data class OrderedProduct(
        val productId: Long,
        val name: String,
        val price: Int,
        val quantity: Int
    )

}