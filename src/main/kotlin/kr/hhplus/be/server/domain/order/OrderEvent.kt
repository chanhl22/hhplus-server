package kr.hhplus.be.server.domain.order

class OrderEvent {
    data class Create(
        val orderId: Long,
        val userId: Long,
        val pointId: Long,
        val products: List<Pair<Long, Int>>,
        val couponId: Long?
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
        fun createProductIdToNameAndQuantityMap(): Map<Long, Pair<String, Int>> {
            return productsDetail.associate { it.productId to (it.name to it.quantity) }
        }
    }

    data class OrderedProduct(
        val productId: Long,
        val name: String,
        val price: Int,
        val quantity: Int
    )

}