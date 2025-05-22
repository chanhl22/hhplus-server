package kr.hhplus.be.server.interfaces.event.coupon

import kr.hhplus.be.server.domain.coupon.CouponCommand
import kr.hhplus.be.server.domain.product.ProductEvent

class CouponEventMapper {
    companion object {
        fun toCommand(event: ProductEvent.Completed): CouponCommand.Use {
            return CouponCommand.Use(
                orderId = event.orderId,
                userId = event.userId,
                pointId = event.pointId,
                products = event.products,
                couponId = event.couponId,
                totalPrice = event.totalPrice,
                productsDetail = event.productsDetail.map {
                    CouponCommand.OrderedProduct(
                        productId = it.productId,
                        name = it.name,
                        price = it.price,
                        quantity = it.quantity
                    )
                }
            )
        }
    }

}