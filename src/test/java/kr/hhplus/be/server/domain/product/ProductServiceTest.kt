package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.fixture.product.ProductCommandFixture
import kr.hhplus.be.server.fixture.product.ProductDomainFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyLong
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

    @InjectMocks
    private lateinit var productService: ProductService

    @DisplayName("상품을 조회한다.")
    @Test
    fun find() {
        //given
        val product = ProductDomainFixture.create()
        BDDMockito.given(productRepository.find(anyLong()))
            .willReturn(product)

        //when
        val result = productService.find(1)

        //then
        assertThat(result)
            .extracting("id", "name", "price", "description", "stock.quantity")
            .containsExactly(product.id, product.name, product.price, product.description, product.stock.quantity)
        Mockito.verify(productRepository, times(1))
            .find(anyLong())
    }

    @DisplayName("주문 가능한 상품들을 조회한다.")
    @Test
    fun findAll() {
        //given
        val products = ProductDomainFixture.createProducts()
        BDDMockito.given(productRepository.findAllWithStockByIdIn(any()))
            .willReturn(products)

        //when
        val productCommand = ProductCommandFixture.createProducts()
        productService.findAll(productCommand)

        //then
        Mockito.verify(productRepository, times(1))
            .findAllWithStockByIdIn(anyList())
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