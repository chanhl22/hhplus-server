package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.fixture.product.ProductDomainFixture
import kr.hhplus.be.server.fixture.stock.StockDomainFixture
import kr.hhplus.be.server.infrastructure.product.ProductJpaRepository
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

}