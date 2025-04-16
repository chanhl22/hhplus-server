package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.order.OrderCommand
import kr.hhplus.be.server.domain.order.coupon.OrderCoupon
import kr.hhplus.be.server.domain.order.OrderPoint
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.order.OrderedProducts
import kr.hhplus.be.server.domain.payment.PaymentCommands.PaymentCommand
import kr.hhplus.be.server.domain.payment.PaymentService
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.stock.StockService
import kr.hhplus.be.server.domain.user.UserService
import org.springframework.stereotype.Service

@Service
class OrderFacade(
    private val productService: ProductService,
    private val stockService: StockService,
    private val orderService: OrderService,
    private val userService: UserService,
    private val paymentService: PaymentService,
    private val pointService: PointService,
    private val couponService: CouponService
) {
    fun order(criteria: OrderCriteria.Order): OrderResult.Order {
        val user = userService.find(criteria.userId)
        val point = pointService.find(user.pointId)

        val products = productService.findAll(criteria.toProductCommand())
        val stocks = stockService.findAll(products.map { it.id })

        val coupon = couponService.find(criteria.couponId, user.id)

        val orderPoint = OrderPoint.create(user, point)
        val orderedProducts = OrderedProducts.create(products, stocks, criteria.createOrderProductQuantityCountMap())
        val orderCoupon = OrderCoupon.from(coupon)
        val order = orderService.order(OrderCommand.of(orderPoint, orderedProducts, orderCoupon))

        couponService.isUsed(order.couponId, user.id)

        val payment = paymentService.process(PaymentCommand.of(user, order))
        if (payment.isSuccess()) {
            pointService.pay(point.id, order.totalPrice)
        }

        return OrderResult.of(point, order, payment)
    }

}