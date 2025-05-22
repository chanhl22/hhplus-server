package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.fixture.order.OrderCommandFixture
import kr.hhplus.be.server.fixture.order.OrderDomainFixture
import kr.hhplus.be.server.fixture.order.OrderProductDomainFixture
import kr.hhplus.be.server.fixture.point.PointDomainFixture
import kr.hhplus.be.server.fixture.product.ProductDomainFixture
import kr.hhplus.be.server.fixture.stock.StockDomainFixture
import kr.hhplus.be.server.fixture.user.UserDomainFixture
import kr.hhplus.be.server.infrastructure.coupon.CouponJpaRepository
import kr.hhplus.be.server.infrastructure.order.OrderJpaRepository
import kr.hhplus.be.server.infrastructure.order.OrderProductJpaRepository
import kr.hhplus.be.server.infrastructure.payment.PaymentJpaRepository
import kr.hhplus.be.server.infrastructure.point.PointJpaRepository
import kr.hhplus.be.server.infrastructure.product.ProductJpaRepository
import kr.hhplus.be.server.infrastructure.product.StockJpaRepository
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate
import java.time.LocalDateTime

@SpringBootTest
class OrderServiceIntegrationTest {

    @Autowired
    private lateinit var orderService: OrderService

    @Autowired
    private lateinit var userJpaRepository: UserJpaRepository

    @Autowired
    private lateinit var pointJpaRepository: PointJpaRepository

    @Autowired
    private lateinit var productJpaRepository: ProductJpaRepository

    @Autowired
    private lateinit var stockJpaRepository: StockJpaRepository

    @Autowired
    private lateinit var couponJpaRepository: CouponJpaRepository

    @Autowired
    private lateinit var orderJpaRepository: OrderJpaRepository

    @Autowired
    private lateinit var orderProductJpaRepository: OrderProductJpaRepository

    @Autowired
    private lateinit var paymentJpaRepository: PaymentJpaRepository

    @Autowired
    private lateinit var redisTemplate: StringRedisTemplate

    @BeforeEach
    fun setUp() {
        redisFlushAll()
    }

    @AfterEach
    fun tearDown() {
        userJpaRepository.deleteAllInBatch()
        pointJpaRepository.deleteAllInBatch()
        productJpaRepository.deleteAllInBatch()
        stockJpaRepository.deleteAllInBatch()
        couponJpaRepository.deleteAllInBatch()
        orderProductJpaRepository.deleteAllInBatch()
        paymentJpaRepository.deleteAllInBatch()
        orderJpaRepository.deleteAllInBatch()
        redisFlushAll()
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

    @DisplayName("주문을 생성한다.")
    @Test
    fun order() {
        //given
        val point = PointDomainFixture.create(pointId = 0L, balance = 5_000_000)
        val savedPoint = pointJpaRepository.save(point)

        val user = UserDomainFixture.create(userId = 0L, pointId = savedPoint.id, balance = savedPoint.balance)
        val savedUser = userJpaRepository.save(user)

        val savedProduct1 = productJpaRepository.save(ProductDomainFixture.create(productId = 0L))
        val savedProduct2 = productJpaRepository.save(ProductDomainFixture.create2(productId = 0L))

        val savedStock1 = stockJpaRepository.save(StockDomainFixture.create(stockId = 0L, productId = savedProduct1.id, 100))
        val savedStock2 = stockJpaRepository.save(StockDomainFixture.create(stockId = 0L, productId = savedProduct2.id, 100))

        val command = OrderCommandFixture.create(
            userId = savedUser.id,
            pointId = savedPoint.id,
            products = listOf(
                OrderCommand.OrderProduct(productId = savedProduct1.id, quantity = 1),
                OrderCommand.OrderProduct(productId = savedProduct2.id, quantity = 1)
            )
        )

        //when
        orderService.ready(command)

        //then
        val findStock1 = stockJpaRepository.findById(savedStock1.id).get()
        assertThat(findStock1.quantity).isEqualTo(99)
        val findStock2 = stockJpaRepository.findById(savedStock2.id).get()
        assertThat(findStock2.quantity).isEqualTo(99)
    }

    @DisplayName("주문 생성 이후 상품 차감을 실패하면 롤백한다.")
    @Test
    fun orderRollback() {
        //given
        val point = PointDomainFixture.create(pointId = 0L, balance = 5_000_000)
        val savedPoint = pointJpaRepository.save(point)

        val user = UserDomainFixture.create(userId = 0L, pointId = savedPoint.id, balance = savedPoint.balance)
        val savedUser = userJpaRepository.save(user)

        val savedProduct1 = productJpaRepository.save(ProductDomainFixture.create(productId = 0L))
        val savedProduct2 = productJpaRepository.save(ProductDomainFixture.create2(productId = 0L))

        val savedStock1 = stockJpaRepository.save(StockDomainFixture.create(stockId = 0L, productId = savedProduct1.id, 100))
        val savedStock2 = stockJpaRepository.save(StockDomainFixture.create(stockId = 0L, productId = savedProduct2.id, 100))

        val command = OrderCommandFixture.create(
            userId = savedUser.id,
            pointId = savedPoint.id,
            products = listOf(
                OrderCommand.OrderProduct(productId = savedProduct1.id, quantity = 101),
                OrderCommand.OrderProduct(productId = savedProduct2.id, quantity = 1)
            )
        )

        //when //then
        assertThatThrownBy { orderService.ready(command) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("주문한 상품 재고가 부족합니다.")

        //then
        val allOrders = orderJpaRepository.findAll()
        assertThat(allOrders).isEmpty()
        val findStock1 = stockJpaRepository.findById(savedStock1.id).get()
        assertThat(findStock1.quantity).isEqualTo(100)
        val findStock2 = stockJpaRepository.findById(savedStock2.id).get()
        assertThat(findStock2.quantity).isEqualTo(100)
    }

    private fun redisFlushAll() {
        redisTemplate.connectionFactory?.connection?.serverCommands()?.flushAll()
    }

}