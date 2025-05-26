package kr.hhplus.be.server.domain.platform

class PlatformCommand {
    data class Payload(
        val orderId: Long,
        val totalPrice: Int,
        val products: List<OrderedProduct>,
        val userId: Long,
        val name: String,
        val couponId: Long?,
        val discountType: String?,
        val discountValue: Int?,
    )

    data class OrderedProduct(
        val productId: Long,
        val name: String,
        val price: Int,
        val quantity: Int
    )

}