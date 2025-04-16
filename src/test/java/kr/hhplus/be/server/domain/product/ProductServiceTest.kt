package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.stock.StockRepository
import kr.hhplus.be.server.fixture.product.ProductCommandFixture
import kr.hhplus.be.server.fixture.product.ProductDomainFixture
import kr.hhplus.be.server.fixture.stock.StockDomainFixture
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any

@ExtendWith(MockitoExtension::class)
class ProductServiceTest {

    @Mock
    private lateinit var productRepository: ProductRepository

    @Mock
    private lateinit var productStatisticsRepository: ProductStatisticsRepository

    @Mock
    private lateinit var stockRepository: StockRepository

    @InjectMocks
    private lateinit var productService: ProductService

    @DisplayName("상품을 조회한다.")
    @Test
    fun find() {
        //given
        val product = ProductDomainFixture.create()
        BDDMockito.given(productRepository.find(any()))
            .willReturn(product)

        val stock = StockDomainFixture.create()
        BDDMockito.given(stockRepository.findByProductId(any()))
            .willReturn(stock)

        //when
        productService.find(1)

        //then
        Mockito.verify(productRepository, times(1))
            .find(any())
        Mockito.verify(stockRepository, times(1))
            .findByProductId(any())
    }

    @DisplayName("주문 가능한 상품들을 조회한다.")
    @Test
    fun findAll() {
        //given
        val products = ProductDomainFixture.createProducts()
        BDDMockito.given(productRepository.findAllByIdIn(any()))
            .willReturn(products)

        //when
        val productCommand = ProductCommandFixture.createProducts()
        productService.findAll(productCommand)

        //then
        Mockito.verify(productRepository, times(1))
            .findAllByIdIn(any())
    }

    @DisplayName("최근 3일간 가장 많이 팔린 상위 5개 상품 정보를 조회한다.")
    @Test
    fun findTopSellingProducts() {
        //given
        val productsStatistics = ProductDomainFixture.createProductsStatistics()
        BDDMockito.given(productStatisticsRepository.findAllByCreatedAtBetween(any(), any()))
            .willReturn(productsStatistics)

        //when
        productService.findTopSellingProducts()

        //then
        Mockito.verify(productStatisticsRepository, times(1))
            .findAllByCreatedAtBetween(any(), any())
    }

}