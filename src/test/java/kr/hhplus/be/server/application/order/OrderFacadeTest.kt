package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.order.OrderCommands
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.payment.Payment
import kr.hhplus.be.server.domain.payment.PaymentCommands
import kr.hhplus.be.server.domain.payment.PaymentService
import kr.hhplus.be.server.domain.point.Point
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.product.ProductCommands
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.product.Stock
import kr.hhplus.be.server.domain.user.User
import kr.hhplus.be.server.domain.user.UserService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class OrderFacadeTest {

    @Mock
    private lateinit var productService: ProductService

    @Mock
    private lateinit var orderService: OrderService

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var paymentService: PaymentService

    @Mock
    private lateinit var pointService: PointService

    @DisplayName("상품을 주문하고 결제한다.")
    @Test
    fun order() {
        //given
        val orderFacade = OrderFacade(productService, orderService, userService, paymentService, pointService)

        val user = User(1L, "이찬희B", Point(1L, 100000))
        BDDMockito.given(userService.findUserWithPointForOrder(ArgumentMatchers.anyLong()))
            .willReturn(user)

        val productCommand = ProductCommands.ProductsCommand.of(
            listOf(
                ProductCommands.ProductCommand(1L, 2),
                ProductCommands.ProductCommand(2L, 1),
            )
        )
        val product1 = Product(1L, "무선 블루투스 이어폰", 129000, "고음질 무선 블루투스 이어폰.", Stock(1L, 25))
        val product2 = Product(2L, "무선 키보드", 375000, "적축 키보드.", Stock(2L, 10))
        BDDMockito.given(productService.findAll(productCommand))
            .willReturn(listOf(product1, product2))

        val orderCommand = OrderCommands.OrderCommand.of(user, listOf(product1, product2))
        val order = Order.create(user, listOf(product1, product2))
        BDDMockito.given(orderService.createOrder(orderCommand))
            .willReturn(order)

        BDDMockito.given(pointService.pay(user.point.id, order.totalPrice))
            .willReturn(user.point)

        val paymentCommand = PaymentCommands.PaymentCommand.of(order)
        val payment = Payment.create(order)
        BDDMockito.given(paymentService.save(paymentCommand))
            .willReturn(payment)

        //when
        val of = OrderCriteria.OrderCriterion.of(
            1L,
            listOf(
                OrderCriteria.OrderProductCriterion(1L, 2),
                OrderCriteria.OrderProductCriterion(2L, 1),
            )
        )
        orderFacade.order(of)

        //then
        Mockito.verify(userService, times(1))
            .findUserWithPointForOrder(anyLong())
        Mockito.verify(productService, times(1))
            .findAll(productCommand)
        Mockito.verify(orderService, times(1))
            .createOrder(orderCommand)
        Mockito.verify(pointService, times(1))
            .pay(user.point.id, order.totalPrice)
        Mockito.verify(paymentService, times(1))
            .save(paymentCommand)
    }

}