package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.payment.PaymentService
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.user.UserService
import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
import kr.hhplus.be.server.fixture.order.OrderDomainsFixture
import kr.hhplus.be.server.fixture.payment.PaymentDomainFixture
import kr.hhplus.be.server.fixture.product.ProductDomainFixture
import kr.hhplus.be.server.fixture.user.UserFixture
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any

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

    @Mock
    private lateinit var couponService: CouponService

    @InjectMocks
    private lateinit var orderFacade: OrderFacade

    @DisplayName("상품을 주문하고 결제한다.")
    @Test
    fun order() {
        //given
        val user = UserFixture.create()
        BDDMockito.given(userService.findUserWithPointForOrder(ArgumentMatchers.anyLong()))
            .willReturn(user)

        val products = ProductDomainFixture.createProducts()
        BDDMockito.given(productService.findAll(any()))
            .willReturn(products)

        val coupon = CouponDomainFixture.create()
        BDDMockito.given(couponService.find(coupon.id, user.id))
            .willReturn(coupon)

        val order = OrderDomainsFixture.create()
        BDDMockito.given(orderService.order(any()))
            .willReturn(order)

        BDDMockito.given(pointService.pay(user.point.id, order.totalPrice))
            .willReturn(user.point)

        val payment = PaymentDomainFixture.create()
        BDDMockito.given(paymentService.process(any()))
            .willReturn(payment)

        //when
        val of = OrderCriteria.OrderCriterion.of(
            1L,
            listOf(
                OrderCriteria.OrderProductCriterion(1L, 2),
                OrderCriteria.OrderProductCriterion(2L, 1),
            ),
            coupon.id
        )
        orderFacade.order(of)

        //then
        Mockito.verify(userService, times(1))
            .findUserWithPointForOrder(anyLong())
        Mockito.verify(productService, times(1))
            .findAll(any())
        Mockito.verify(couponService, times(1))
            .find(anyLong(), anyLong())
        Mockito.verify(orderService, times(1))
            .order(any())
        Mockito.verify(pointService, times(1))
            .pay(anyLong(), anyInt())
        Mockito.verify(paymentService, times(1))
            .process(any())
    }

}