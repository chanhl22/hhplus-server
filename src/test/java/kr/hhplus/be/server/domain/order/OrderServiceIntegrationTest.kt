package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.order.coupon.EmptyOrderCoupon
import kr.hhplus.be.server.fixture.order.OrderDomainFixture
import kr.hhplus.be.server.fixture.order.OrderProductDomainFixture
import kr.hhplus.be.server.fixture.point.PointDomainFixture
import kr.hhplus.be.server.fixture.product.ProductDomainFixture
import kr.hhplus.be.server.fixture.stock.StockDomainFixture
import kr.hhplus.be.server.fixture.user.UserDomainFixture
import kr.hhplus.be.server.infrastructure.order.OrderJpaRepository
import kr.hhplus.be.server.infrastructure.order.OrderProductJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
@SpringBootTest
class OrderServiceIntegrationTest {

    @Autowired
    private lateinit var orderService: OrderService

    @Autowired
    private lateinit var orderJpaRepository: OrderJpaRepository

    @Autowired
    private lateinit var orderProductJpaRepository: OrderProductJpaRepository

    @DisplayName("주문을 저장한다.")
    @Test
    fun order() {
        //given
        val user = UserDomainFixture.create(userId = 1L)
        val point = PointDomainFixture.create(pointId = 1L, balance = 5_000_000)
        val orderPoint = OrderPoint.create(user, point)

        val products = ProductDomainFixture.createProducts(productId1 = 1L, productId2 = 2L)
        val stocks = StockDomainFixture.createStocks(stockId1 = 1L, stockId2 = 2L, productId1 = 1L, productId2 = 2L)
        val orderedProducts = OrderedProducts.create(products, stocks, mapOf(1L to 3, 2L to 2))

        val orderCoupon = EmptyOrderCoupon

        //when
        val result = orderService.order(OrderCommand.of(orderPoint, orderedProducts, orderCoupon))

        //then
        assertThat(result)
            .extracting("userId", "totalPrice", "couponId")
            .containsExactly(
                1L, 387000 + 750000, null
            )
        assertThat(result.orderProducts).hasSize(2)
            .extracting("productId")
            .containsExactly(
                1L, 2L
            )
    }

    @DisplayName("통계를 위해 주문을 집계한다.")
    @Test
    fun aggregateOrderProduct() {
        //given
        val order = OrderDomainFixture.create(
            orderId = 0L,
            registeredAt = LocalDateTime.now().minusDays(1)
        )
        val savedOrder = orderJpaRepository.save(order)

        val orderProduct = OrderProductDomainFixture.create(order = savedOrder, productId = 1L)
        orderProductJpaRepository.save(orderProduct)

        //when
        val result = orderService.aggregateOrderProduct()

        //then
        assertThat(result).hasSize(1)
            .extracting("productId", "totalSales")
            .containsExactly(
                Tuple.tuple(1L, 1)
            )
    }

}