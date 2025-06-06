package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
import kr.hhplus.be.server.fixture.order.OrderCriteriaFixture
import kr.hhplus.be.server.fixture.point.PointDomainFixture
import kr.hhplus.be.server.fixture.product.ProductDomainFixture
import kr.hhplus.be.server.fixture.stock.StockDomainFixture
import kr.hhplus.be.server.fixture.user.UserDomainFixture
import kr.hhplus.be.server.infrastructure.coupon.CouponJpaRepository
import kr.hhplus.be.server.infrastructure.coupon.UserCouponJpaRepository
import kr.hhplus.be.server.infrastructure.order.OrderJpaRepository
import kr.hhplus.be.server.infrastructure.order.OrderProductJpaRepository
import kr.hhplus.be.server.infrastructure.payment.PaymentJpaRepository
import kr.hhplus.be.server.infrastructure.point.PointJpaRepository
import kr.hhplus.be.server.infrastructure.product.ProductJpaRepository
import kr.hhplus.be.server.infrastructure.product.StockJpaRepository
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class OrderFacadeConcurrencyTest {

    @Autowired
    private lateinit var orderFacade: OrderFacade

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
    private lateinit var userCouponJpaRepository: UserCouponJpaRepository

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
        userCouponJpaRepository.deleteAllInBatch()
        couponJpaRepository.deleteAllInBatch()
        orderProductJpaRepository.deleteAllInBatch()
        paymentJpaRepository.deleteAllInBatch()
        orderJpaRepository.deleteAllInBatch()
        redisFlushAll()
    }

    @DisplayName("주문을 동시에 요청해도 상품 재고는 누락되지 않고 차감된다.")
    @Test
    fun lostOrder() {
        //given
        val point = PointDomainFixture.create(pointId = 0L, balance = 2000000)
        val savedPoint = pointJpaRepository.save(point)

        val user = UserDomainFixture.create(userId = 0L, pointId = savedPoint.id, balance = 2000000)
        val savedUser = userJpaRepository.save(user)

        val product1 = ProductDomainFixture.create(productId = 0L, price = 1000)
        val savedProduct1 = productJpaRepository.save(product1)
        val product2 = ProductDomainFixture.create(productId = 0L, price = 1000)
        val savedProduct2 = productJpaRepository.save(product2)

        val stock1 = StockDomainFixture.create(stockId = 0L, productId = savedProduct1.id, quantity = 10)
        val savedStock1 = stockJpaRepository.save(stock1)
        val stock2 = StockDomainFixture.create(stockId = 0L, productId = savedProduct2.id, quantity = 10)
        val savedStock2 = stockJpaRepository.save(stock2)

        val coupon = CouponDomainFixture.create(couponId = 0L)
        couponJpaRepository.save(coupon)

        val threadCount = 10
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        val orderProduct1 = OrderCriteriaFixture.create(productId = savedProduct1.id, quantity = 1)
        val orderProduct2 = OrderCriteriaFixture.create(productId = savedProduct2.id, quantity = 1)
        val criteria = OrderCriteriaFixture.create(
            userId = savedUser.id,
            products = listOf(orderProduct1, orderProduct2),
            couponId = coupon.id
        )

        //when
        for (idx in 1..threadCount) {
            executorService.execute {
                try {
                    orderFacade.order(criteria)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val findStock1 = stockJpaRepository.findById(savedStock1.id)
        val findStock2 = stockJpaRepository.findById(savedStock2.id)
        assertThat(findStock1.get().quantity).isEqualTo(0)
        assertThat(findStock2.get().quantity).isEqualTo(0)
    }

    private fun redisFlushAll() {
        redisTemplate.connectionFactory?.connection?.serverCommands()?.flushAll()
    }

}