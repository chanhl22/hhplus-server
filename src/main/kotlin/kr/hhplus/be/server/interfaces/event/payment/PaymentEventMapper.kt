package kr.hhplus.be.server.interfaces.event.payment

import kr.hhplus.be.server.domain.payment.PaymentCommand
import kr.hhplus.be.server.domain.point.PointEvent

class PaymentEventMapper {
    companion object {
        fun toCommand(event: PointEvent.Completed): PaymentCommand.Save {
            return PaymentCommand.Save(
                orderId = event.orderId,
                userId = event.userId,
                pointId = event.pointId,
                products = event.products,
                couponId = event.couponId,
                totalPrice = event.totalPrice,
                productsDetail = event.productsDetail.map {
                    PaymentCommand.OrderedProduct(
                        productId = it.productId,
                        name = it.name,
                        price = it.price,
                        quantity = it.quantity
                    )
                },
                discountType = event.discountType,
                discountValue = event.discountValue
            )
        }
    }

}