package kr.hhplus.be.server.interfaces.event.platform

import kr.hhplus.be.server.domain.order.OrderEvent
import kr.hhplus.be.server.domain.platform.PlatformCommand

class PlatformOrderEventMapper {
    companion object {
        fun toCommand(event: OrderEvent.Completed): PlatformCommand.Payload {
            return PlatformCommand.Payload(
                orderId = event.orderId,
                totalPrice = event.totalPrice,
                products = event.products.map {
                    PlatformCommand.OrderedProduct(
                        productId = it.productId,
                        name = it.name,
                        price = it.price,
                        quantity = it.quantity
                    )
                },
                userId = event.userId,
                name = event.name,
                couponId = event.couponId,
                discountType = event.discountType,
                discountValue = event.discountValue
            )
        }
    }

}