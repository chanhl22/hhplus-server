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
import org.springframework.retry.annotation.EnableRetry
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
@EnableRetry
class ProductServiceConcurrencyRetryTest {

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

    @DisplayName("낙관적 락 예외가 발생하면 재시도된다")
    @Test
    fun retryIssueProduct() {
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

        val errors = mutableListOf<Throwable>()

        //when
        for (idx in 1..threadCount) {
            executorService.execute {
                try {
                    productService.deduct(command)
                } catch (e: ObjectOptimisticLockingFailureException) {
                    errors.add(e)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        assertThat(errors)
            .allSatisfy { error ->
                assertThat(error).isInstanceOf(ObjectOptimisticLockingFailureException::class.java)
            }
    }

}