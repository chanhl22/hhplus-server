package kr.hhplus.be.server.interfaces.event.order

import kr.hhplus.be.server.domain.order.OrderCommand
import kr.hhplus.be.server.domain.payment.PaymentEvent

class OrderEventMapper {
    companion object {
        fun toCommand(event: PaymentEvent.Completed): OrderCommand.Completed {
            return OrderCommand.Completed(
                orderId = event.orderId,
                userId = event.userId,
                pointId = event.pointId,
                products = event.products,
                couponId = event.couponId,
                totalPrice = event.totalPrice,
                productsDetail = event.productsDetail.map {
                    OrderCommand.OrderedProduct(
                        productId = it.productId,
                        name = it.name,
                        price = it.price,
                        quantity = it.quantity
                    )
                },
                discountType = event.discountType,
                discountValue = event.discountValue,
            )
        }
    }

}