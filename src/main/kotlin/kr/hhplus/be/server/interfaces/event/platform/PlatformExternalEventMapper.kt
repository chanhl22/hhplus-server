package kr.hhplus.be.server.interfaces.event.platform

import kr.hhplus.be.server.domain.order.OrderEvent
import kr.hhplus.be.server.domain.platform.PlatformCommand

class PlatformExternalEventMapper {
    companion object {
        fun toCommand(event: OrderEvent.Completed): PlatformCommand.Payload {
            return PlatformCommand.Payload(
                orderId = event.orderId,
                totalPrice = event.totalPrice,
                products = event.productsDetail.map {
                    PlatformCommand.OrderedProduct(
                        productId = it.productId,
                        name = it.name,
                        price = it.price,
                        quantity = it.quantity
                    )
                },
                userId = event.userId,
                couponId = event.couponId,
                discountType = event.discountType,
                discountValue = event.discountValue
            )
        }
    }

}