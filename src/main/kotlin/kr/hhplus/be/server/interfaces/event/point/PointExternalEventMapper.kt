package kr.hhplus.be.server.interfaces.event.point

import kr.hhplus.be.server.domain.coupon.CouponEvent
import kr.hhplus.be.server.domain.point.PointCommand

class PointExternalEventMapper {
    companion object {
        fun toCommand(event: CouponEvent.Completed): PointCommand.Deduct {
            return PointCommand.Deduct(
                orderId = event.orderId,
                userId = event.userId,
                pointId = event.pointId,
                products = event.products,
                couponId = event.couponId,
                totalPrice = event.totalPrice,
                productsDetail = event.productsDetail.map {
                    PointCommand.OrderedProduct(
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