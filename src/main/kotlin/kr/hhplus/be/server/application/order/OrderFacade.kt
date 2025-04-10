package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.application.order.OrderCriteria.OrderCriterion
import kr.hhplus.be.server.application.order.OrderResults.OrderResult
import kr.hhplus.be.server.domain.order.OrderCommands.OrderCommand
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.payment.PaymentCommands.PaymentCommand
import kr.hhplus.be.server.domain.payment.PaymentService
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.user.UserService
import org.springframework.stereotype.Service

@Service
class OrderFacade(
    private val productService: ProductService,
    private val orderService: OrderService,
    private val userService: UserService,
    private val paymentService: PaymentService,
    private val pointService: PointService
) {
    fun order(criterion: OrderCriterion): OrderResult {
        val user = userService.findUserWithPointForOrder(criterion.userId)

        val products = productService.findAll(criterion.toProductCommand())

        val order = orderService.createOrder(OrderCommand.of(user, products))

        pointService.pay(user.point.id, order.totalPrice)
        val payment = paymentService.save(PaymentCommand.of(order))

        return OrderResult.of(user, order, payment)
    }

}