package kr.hhplus.be.server.fixture.order

import kr.hhplus.be.server.application.order.OrderResults
import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.order.OrderProduct
import kr.hhplus.be.server.domain.payment.Payment
import kr.hhplus.be.server.domain.payment.PaymentStatus
import kr.hhplus.be.server.domain.point.Point
import kr.hhplus.be.server.domain.user.User
import java.time.LocalDateTime

object OrderResultFixture {
    fun create(
        userId: Long = 1L,
        username: String = "이찬희B",
        pointId: Long = 1L,
        balance: Int = 100000,
        orderId: Long = 1L,
        totalPrice: Int = 10000,
        registeredDateTime: LocalDateTime = LocalDateTime.now(),
        orderProducts: MutableList<OrderProduct> = mutableListOf(),
        paymentId: Long = 1L,
        paymentAmount: Int = 10000
    ): OrderResults.OrderResult {
        val user = User(
            id = userId,
            name = username,
            point = Point(
                id = pointId,
                balance = balance
            )
        )
        val order = Order(
            id = orderId,
            user = user,
            totalPrice = totalPrice,
            registeredDateTime = registeredDateTime,
            orderProducts = orderProducts
        )

        return OrderResults.OrderResult(
            user = user,
            order = order,
            payment = Payment(
                id = paymentId,
                order = order,
                amount = paymentAmount,
                paymentStatus = PaymentStatus.SUCCEEDED
            )
        )
    }
}