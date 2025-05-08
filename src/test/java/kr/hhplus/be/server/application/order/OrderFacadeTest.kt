package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.payment.PaymentService
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.user.UserService
import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
import kr.hhplus.be.server.fixture.order.OrderCriteriaFixture
import kr.hhplus.be.server.fixture.order.OrderDomainFixture
import kr.hhplus.be.server.fixture.payment.PaymentDomainFixture
import kr.hhplus.be.server.fixture.point.PointDomainFixture
import kr.hhplus.be.server.fixture.product.ProductInfoFixture
import kr.hhplus.be.server.fixture.user.UserDomainFixture
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
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
        val user = UserDomainFixture.create()
        BDDMockito.given(userService.find(any()))
            .willReturn(user)

        val point = PointDomainFixture.create()
        BDDMockito.given(pointService.find(any()))
            .willReturn(point)

        val products = ProductInfoFixture.createProducts()
        BDDMockito.given(productService.findAll(any()))
            .willReturn(products)

        val coupon = CouponDomainFixture.create()
        BDDMockito.given(couponService.find(any(), any()))
            .willReturn(coupon)

        val order = OrderDomainFixture.create()
        BDDMockito.given(orderService.order(any()))
            .willReturn(order)

        BDDMockito.willDoNothing()
            .given(couponService)
            .isUsed(any(), any())

        val payment = PaymentDomainFixture.create()

        BDDMockito.given(paymentService.process(any()))
            .willReturn(payment)

        BDDMockito.willDoNothing()
            .given(pointService)
            .use(any(), any())

        //when
        val criteria = OrderCriteriaFixture.create(userId = 1L)
        orderFacade.order(criteria)

        //then
        Mockito.verify(userService, times(1))
            .find(any())
        Mockito.verify(productService, times(1))
            .findAll(any())
        Mockito.verify(couponService, times(1))
            .find(any(), any())
        Mockito.verify(couponService, times(1))
            .isUsed(any(), any())
        Mockito.verify(orderService, times(1))
            .order(any())
        Mockito.verify(pointService, times(1))
            .use(any(), any())
        Mockito.verify(paymentService, times(1))
            .process(any())
    }

}