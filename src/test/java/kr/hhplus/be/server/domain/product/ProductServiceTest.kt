package kr.hhplus.be.server.domain.product

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ProductServiceTest {

    @Mock
    private lateinit var productRepository: ProductRepository

    @DisplayName("상품을 조회한다.")
    @Test
    fun find() {
        //given
        val productService = ProductService(productRepository)

        val product = Product(1L, "무선 블루투스 이어폰", 129000, "고음질 무선 블루투스 이어폰. 최대 20시간 사용 가능.", Stock(1L, 25))
        BDDMockito.given(productRepository.find(ArgumentMatchers.anyLong()))
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
        val productService = ProductService(productRepository)

        val product1 = Product(1L, "무선 블루투스 이어폰", 129000, "고음질 무선 블루투스 이어폰.", Stock(1L, 25))
        val product2 = Product(2L, "무선 키보드", 375000, "적축 키보드.", Stock(2L, 10))
        BDDMockito.given(productRepository.findAllWithStockByIdIn(listOf(1L, 2L)))
            .willReturn(listOf(product1, product2))

        //when
        val productCommand = ProductCommands.ProductsCommand.of(
            listOf(
                ProductCommands.ProductCommand(1L, 2),
                ProductCommands.ProductCommand(2L, 1),
            )
        )
        productService.findAll(productCommand)

        //then
        Mockito.verify(productRepository, times(1))
            .findAllWithStockByIdIn(listOf(1L, 2L))
    }

}