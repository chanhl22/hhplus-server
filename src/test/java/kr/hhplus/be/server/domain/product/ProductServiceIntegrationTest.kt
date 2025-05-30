package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.fixture.product.ProductCommandFixture
import kr.hhplus.be.server.fixture.product.ProductDomainFixture
import kr.hhplus.be.server.fixture.stock.StockDomainFixture
import kr.hhplus.be.server.infrastructure.product.ProductJpaRepository
import kr.hhplus.be.server.infrastructure.product.ProductStatisticsJpaRepository
import kr.hhplus.be.server.infrastructure.product.StockJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class ProductServiceIntegrationTest {

    @Autowired
    private lateinit var productStatisticsJpaRepository: ProductStatisticsJpaRepository

    @Autowired
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var productJpaRepository: ProductJpaRepository

    @Autowired
    private lateinit var stockJpaRepository: StockJpaRepository

    @Autowired
    private lateinit var cacheManager: CacheManager

    @MockitoBean
    private lateinit var productEventPublisher: ProductEventPublisher

    @AfterEach
    fun tearDown() {
        cacheManager.getCache("findTopSellingProducts")?.clear()
    }

    @DisplayName("상품 정보를 조회한다.")
    @Test
    fun find() {
        //given
        val product = ProductDomainFixture.create(productId = 0L)
        val savedProduct = productJpaRepository.save(product)

        val stock = StockDomainFixture.create(stockId = 0L, productId = savedProduct.id, quantity = 20)
        stockJpaRepository.save(stock)

        //when
        val result = productService.find(savedProduct.id)

        //then
        assertThat(result)
            .extracting("name", "price", "description", "quantity")
            .containsExactly(
                "무선 블루투스 이어폰", 129000, "고음질 무선 블루투스 이어폰.", 20
            )
    }

    @DisplayName("상품 재고를 차감한다.")
    @Test
    fun deduct() {
        //given
        val product1 = ProductDomainFixture.create(0L)
        val savedProduct1 = productJpaRepository.save(product1)
        val product2 = ProductDomainFixture.create(0L)
        val savedProduct2 = productJpaRepository.save(product2)

        val stock1 = StockDomainFixture.create(stockId = 0L, productId = savedProduct1.id)
        val savedStock1 = stockJpaRepository.save(stock1)
        val stock2 = StockDomainFixture.create(stockId = 0L, productId = savedProduct2.id)
        val savedStock2 = stockJpaRepository.save(stock2)

        val command = ProductCommandFixture.create(
            products = listOf(savedProduct1.id to 2, savedProduct2.id to 2)
        )

        //when
        productService.deduct(command)

        //then
        val findStock1 = stockJpaRepository.findById(savedStock1.id)
        assertThat(findStock1.get().quantity).isEqualTo(9998)

        val findStock2 = stockJpaRepository.findById(savedStock2.id)
        assertThat(findStock2.get().quantity).isEqualTo(9998)
    }

    @DisplayName("주문 통계를 저장한다.")
    @Test
    fun saveProductStatistics() {
        //given
        val command = listOf(
            ProductCommand.of(productId = 1L, totalSales = 10000)
        )

        //when
        productService.saveProductStatistics(command)

        //then
        val productsStatistics = productStatisticsJpaRepository.findAll()
        assertThat(productsStatistics).hasSize(1)
            .extracting("productId", "totalSales")
            .containsExactly(
                Tuple.tuple(1L, 10000)
            )
    }

    @DisplayName("최근 3일간 가장 많이 팔린 상위 5개 상품 정보를 조회한다.")
    @Test
    fun findTopSellingProducts() {
        //given
        val products = ProductDomainFixture.createProducts(productId1 = 0L, productId2 = 0L)
        val savedProducts = productJpaRepository.saveAll(products)

        val productStatistics =
            ProductDomainFixture.createProductsStatistics(
                productStatisticsId1 = 0L,
                productStatisticsId2 = 0L,
                productStatisticsId3 = 0L,
                productStatisticsId4 = 0L,
                productStatisticsId5 = 0L,
                productStatisticsId6 = 0L,
                productId1 = savedProducts[0].id,
                productId2 = savedProducts[1].id,
            )
        productStatisticsJpaRepository.saveAll(productStatistics)

        //when
        val result = productService.findTopSellingProducts()

        //then
        assertThat(result).hasSize(2)
            .extracting("name", "price", "soldQuantity", "rank")
            .containsExactly(
                Tuple.tuple("무선 키보드", 375000, 900, 1),
                Tuple.tuple("무선 블루투스 이어폰", 129000, 500, 2)
            )
    }

    @DisplayName("캐시가 비워졌는지 확인한다.")
    @Test
    fun clearProductStatisticsCache() {
        //given
        val cache = cacheManager.getCache("findTopSellingProducts")
        cache?.put("findTopSellingProducts", "test")

        // when
        productService.clearProductStatisticsCache()

        // then
        val result = cache?.get("findTopSellingProducts", String::class.java)
        assertThat(result).isNull()
    }

}