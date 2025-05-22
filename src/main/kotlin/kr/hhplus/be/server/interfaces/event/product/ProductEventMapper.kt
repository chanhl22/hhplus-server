package kr.hhplus.be.server.interfaces.event.product

import kr.hhplus.be.server.domain.order.OrderEvent
import kr.hhplus.be.server.domain.product.ProductCommand

class ProductEventMapper {
    companion object {
        fun toCommand(event: OrderEvent.Create): ProductCommand.Deduct {
            return ProductCommand.Deduct(
                orderId = event.orderId,
                userId = event.userId,
                pointId = event.pointId,
                products = event.products,
                couponId = event.couponId
            )
        }
    }

}