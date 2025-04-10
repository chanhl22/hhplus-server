package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.fixture.product.ProductDomainFixture
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProductTest {

    @DisplayName("재고가 부족하다면 예외가 발생한다.")
    @Test
    fun validateStockEnough() {
        //given
        val productId = 1L
        val product = ProductDomainFixture.create(productId = productId, quantity = 2)
        val orderQuantity = 3

        //when //then
        assertThatThrownBy { product.validateStockEnough(orderQuantity) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("재고가 부족한 상품이 있습니다. id: $productId")
    }

    @DisplayName("재고가 부족하지 않으면 예외가 발생하지 않는다.")
    @Test
    fun validateStockEnough2() {
        //given
        val productId = 1L
        val product = ProductDomainFixture.create(productId = productId, quantity = 2)
        val orderQuantity = 2

        // When & Then
        assertThatCode { product.validateStockEnough(orderQuantity) }
            .doesNotThrowAnyException()
    }

}