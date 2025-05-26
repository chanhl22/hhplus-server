package kr.hhplus.be.server.domain.platform

class PlatformOrderMapper {
    companion object {
        fun toDomain(command: PlatformCommand.Payload): PlatformOrder {
            return PlatformOrder.create(
                orderId = command.orderId,
                totalPrice = command.totalPrice,
                products = command.products.map {
                    PlatformProductOrder(
                        productId = it.productId,
                        name = it.name,
                        price = it.price,
                        quantity = it.quantity
                    )
                },
                userId = command.userId,
                name = command.name,
                couponId = command.couponId,
                discountType = command.discountType,
                discountValue = command.discountValue
            )
        }
    }

}