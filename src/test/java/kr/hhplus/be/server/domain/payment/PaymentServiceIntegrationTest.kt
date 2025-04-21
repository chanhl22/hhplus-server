package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.domain.order.OrderCommand
import kr.hhplus.be.server.domain.order.OrderPoint
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.order.OrderedProducts
import kr.hhplus.be.server.domain.order.coupon.EmptyOrderCoupon
import kr.hhplus.be.server.fixture.point.PointDomainFixture
import kr.hhplus.be.server.fixture.product.ProductDomainFixture
import kr.hhplus.be.server.fixture.stock.StockDomainFixture
import kr.hhplus.be.server.fixture.user.UserDomainFixture
import kr.hhplus.be.server.infrastructure.payment.PaymentJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class PaymentServiceIntegrationTest {

    @Autowired
    private lateinit var paymentService: PaymentService

    @Autowired
    private lateinit var orderService: OrderService

    @Autowired
    private lateinit var paymentJpaRepository: PaymentJpaRepository

    @DisplayName("주문서를 바탕으로 결제를 처리한다.")
    @Test
    fun process() {
        //given
        val user = UserDomainFixture.create(userId = 1L)
        val point = PointDomainFixture.create(pointId = 1L, balance = 5_000_000)
        val orderPoint = OrderPoint.create(user, point)

        val products = ProductDomainFixture.createProducts(productId1 = 1L, productId2 = 2L)
        val stocks = StockDomainFixture.createStocks(stockId1 = 1L, stockId2 = 2L, productId1 = 1L, productId2 = 2L)
        val orderedProducts = OrderedProducts.create(products, stocks, mapOf(1L to 3, 2L to 2))

        val orderCoupon = EmptyOrderCoupon

        val order = orderService.order(OrderCommand.of(orderPoint, orderedProducts, orderCoupon))

        //when
        val command = PaymentCommand.of(point, order)
        val result = paymentService.process(command)

        //then
        val findPayment = paymentJpaRepository.findById(result.id)
        assertThat(findPayment.get())
            .extracting("amount", "paymentStatus")
            .containsExactly(order.totalPrice, PaymentStatus.SUCCEEDED)
    }

}