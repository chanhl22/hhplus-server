package kr.hhplus.be.server.domain.platform

class PlatformOrder(
    val orderId: Long,
    val totalPrice: Int,
    val products: List<PlatformProductOrder>,
    val userId: Long,
    val name: String,
    val couponId: Long?,
    val discountType: String?,
    val discountValue: Int?,
) {
    companion object {
        fun create(
            orderId: Long,
            totalPrice: Int,
            products: List<PlatformProductOrder>,
            userId: Long,
            name: String,
            couponId: Long?,
            discountType: String?,
            discountValue: Int?
        ): PlatformOrder {
            return PlatformOrder(
                orderId = orderId,
                totalPrice = totalPrice,
                products = products,
                userId = userId,
                name = name,
                couponId = couponId,
                discountType = discountType,
                discountValue = discountValue
            )

        }
    }

}

class PlatformProductOrder(
    val productId: Long,
    val name: String,
    val price: Int,
    val quantity: Int
)