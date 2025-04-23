package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.fixture.product.ProductDomainFixture
import kr.hhplus.be.server.fixture.stock.StockDomainFixture
import kr.hhplus.be.server.infrastructure.product.ProductJpaRepository
import kr.hhplus.be.server.infrastructure.product.StockJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class ProductServiceConcurrencyTest {

    @Autowired
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var productJpaRepository: ProductJpaRepository

    @Autowired
    private lateinit var stockJpaRepository: StockJpaRepository

    @BeforeEach
    fun setUp() {
        val product = ProductDomainFixture.create(productId = 0L)
        productJpaRepository.saveAndFlush(product)

        val stock = StockDomainFixture.create(
            stockId = 0L,
            productId = 1L,
            quantity = 100
        )
        stockJpaRepository.saveAndFlush(stock)
    }

    @AfterEach
    fun tearDown() {
        productJpaRepository.deleteAllInBatch()
        stockJpaRepository.deleteAllInBatch()
    }

    @DisplayName("재고가 동시에 차감될 수 없다.")
    @Test
    fun deduct() {
        //given
        val command = ProductCommand.Deduct(
            listOf(
                ProductCommand.OrderProduct(productId = 1L, 1)
            )
        )

        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        //when
        for (idx in 1..threadCount) {
            executorService.execute {
                try {
                    productService.deduct(command)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val findStock = stockJpaRepository.findById(1L)
        assertThat(findStock.get().quantity).isEqualTo(0)
    }

}