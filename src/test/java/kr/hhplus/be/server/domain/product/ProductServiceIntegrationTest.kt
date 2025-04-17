package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.fixture.product.ProductDomainFixture
import kr.hhplus.be.server.fixture.stock.StockDomainFixture
import kr.hhplus.be.server.infrastructure.product.ProductJpaRepository
import kr.hhplus.be.server.infrastructure.product.ProductStatisticsJpaRepository
import kr.hhplus.be.server.infrastructure.product.StockJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
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

    @DisplayName("상품 정보들을 조회한다.")
    @Test
    fun findAll() {
        //given
        val products = ProductDomainFixture.createProducts(productId1 = 0L, productId2 = 0L)
        val savedProducts = productJpaRepository.saveAll(products)

        val stocks = StockDomainFixture.createStocks(stockId1 = 0L, stockId2 = 0L)
        stockJpaRepository.saveAll(stocks)

        //when
        val result = productService.findAll(savedProducts.map { it.id })

        //then
        assertThat(result.products).hasSize(2)
            .extracting("name", "price", "description")
            .containsExactly(
                Tuple.tuple("무선 블루투스 이어폰", 129000, "고음질 무선 블루투스 이어폰."),
                Tuple.tuple("무선 키보드", 375000, "적축 키보드.")
            )
        assertThat(result.stocks).hasSize(2)
            .extracting("productId", "quantity")
            .containsExactly(
                Tuple.tuple(1L, 25),
                Tuple.tuple(2L, 10)
            )
    }

    @DisplayName("상품 재고를 차감한다.")
    @Test
    fun deduct() {
        //given
        val products = ProductDomainFixture.createProducts(productId1 = 0L, productId2 = 0L)
        val savedProducts = productJpaRepository.saveAll(products)

        val stocks = StockDomainFixture.createStocks(stockId1 = 0L, stockId2 = 0L, quantity1 = 25, quantity2 = 10)
        stockJpaRepository.saveAll(stocks)

        //when
        val command = ProductCommand.Deduct(savedProducts.map { product ->
            ProductCommand.OrderProduct(productId = product.id, 2)
        })
        productService.deduct(command)

        //then
        val stock1 = stockJpaRepository.findById(stocks[0].id)
        assertThat(stock1.get().quantity).isEqualTo(23)

        val stock2 = stockJpaRepository.findById(stocks[1].id)
        assertThat(stock2.get().quantity).isEqualTo(8)
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

}