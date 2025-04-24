package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.fixture.product.ProductDomainFixture
import kr.hhplus.be.server.fixture.stock.StockDomainFixture
import kr.hhplus.be.server.infrastructure.product.ProductJpaRepository
import kr.hhplus.be.server.infrastructure.product.StockJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.orm.ObjectOptimisticLockingFailureException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
class ProductServiceConcurrencyTest {

    @Autowired
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var productJpaRepository: ProductJpaRepository

    @Autowired
    private lateinit var stockJpaRepository: StockJpaRepository

    @AfterEach
    fun tearDown() {
        productJpaRepository.deleteAllInBatch()
        stockJpaRepository.deleteAllInBatch()
    }

    @DisplayName("주문을 동시에 요청해 재고가 동시에 차감될 때 누락되지 않고 차감된다.")
    @Test
    fun lostIssueProduct() {
        //given
        val product = ProductDomainFixture.create(productId = 0L)
        val savedProduct = productJpaRepository.saveAndFlush(product)

        val stock = StockDomainFixture.create(
            stockId = 0L,
            productId = savedProduct.id,
            quantity = 100
        )
        stockJpaRepository.saveAndFlush(stock)

        val command = ProductCommand.Deduct(
            listOf(
                ProductCommand.OrderProduct(productId = savedProduct.id, quantity = 1)
            )
        )

        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        val exceptionCount = AtomicInteger(0)

        //when
        for (idx in 1..threadCount) {
            executorService.execute {
                try {
                    productService.deduct(command)
                } catch (e: ObjectOptimisticLockingFailureException) {
                    exceptionCount.incrementAndGet()
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        assertThat(exceptionCount.get()).isGreaterThan(0)
    }

    @DisplayName("동시에 두 명이 동일한 상품을 주문해도 재고가 음수가 되지 않아야 한다.")
    @Test
    fun productStockQuantityAlwaysPositive() {
        val product = ProductDomainFixture.create(productId = 0L)
        val savedProduct = productJpaRepository.saveAndFlush(product)

        val stock = StockDomainFixture.create(
            stockId = 0L,
            productId = savedProduct.id,
            quantity = 1
        )
        val savedStock = stockJpaRepository.saveAndFlush(stock)

        val command = ProductCommand.Deduct(
            listOf(
                ProductCommand.OrderProduct(productId = savedProduct.id, quantity = 1)
            )
        )

        val threadCount = 2
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(2)

        val exceptionCount = AtomicInteger(0)

        //when
        for (idx in 1..threadCount) {
            executorService.execute {
                try {
                    productService.deduct(command)
                } catch (e: ObjectOptimisticLockingFailureException) {
                    exceptionCount.incrementAndGet()
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val findStock = stockJpaRepository.findById(savedStock.id)
        assertThat(findStock.get().quantity).isEqualTo(0)
        assertThat(exceptionCount.get()).isEqualTo(1)
    }

}