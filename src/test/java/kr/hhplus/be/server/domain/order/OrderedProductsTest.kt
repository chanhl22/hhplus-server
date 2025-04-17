package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.fixture.product.ProductDomainFixture
import kr.hhplus.be.server.fixture.stock.StockDomainFixture
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class OrderedProductsTest {

    @DisplayName("주문을 생성한다.")
    @Test
    fun create() {
        //given
        val products = listOf(
            ProductDomainFixture.create(
                productId = 1L,
                price = 129000
            ),
            ProductDomainFixture.create(
                productId = 2L,
                price = 100
            )
        )
        val stocks = listOf(
            StockDomainFixture.create(
                stockId = 1L,
                productId = 1L,
                quantity = 100
            ),
            StockDomainFixture.create(
                stockId = 2L,
                productId = 2L,
                quantity = 50
            )
        )
        val orderProductQuantityCountMap = mapOf(
            1L to 2,
            2L to 1
        )

        //when
        val result = OrderedProducts.create(products, stocks, orderProductQuantityCountMap)

        //then
        assertThat(result.products).hasSize(2)
            .extracting("productId", "price", "quantity")
            .containsExactly(
                Tuple.tuple(1L, 129000, 100),
                Tuple.tuple(2L, 100, 50)
            )
        assertThat(result.orderProductQuantityCountMap).isEqualTo(orderProductQuantityCountMap)
    }

    @DisplayName("주문이 비어있으면 예외가 발생한다.")
    @Test
    fun isEmptyOrder() {
        //given
        val products = listOf(
            ProductDomainFixture.create(
                productId = 1L,
                price = 129000
            ),
            ProductDomainFixture.create(
                productId = 2L,
                price = 100
            )
        )
        val stocks = listOf(
            StockDomainFixture.create(
                stockId = 1L,
                productId = 1L,
                quantity = 100
            ),
            StockDomainFixture.create(
                stockId = 2L,
                productId = 2L,
                quantity = 50
            )
        )
        val orderProductQuantityCountMap = emptyMap<Long, Int>()

        val result = OrderedProducts.create(products, stocks, orderProductQuantityCountMap)

        //when //then
        assertThatThrownBy { result.isEmptyOrder() }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("주문 항목이 비어있습니다.")
    }

    @DisplayName("주문 상품들의 총 합을 계산한다.")
    @Test
    fun calculateTotalPrice() {
        //given
        val products = listOf(
            ProductDomainFixture.create(
                productId = 1L,
                price = 129000
            ),
            ProductDomainFixture.create(
                productId = 2L,
                price = 100
            )
        )
        val stocks = listOf(
            StockDomainFixture.create(
                stockId = 1L,
                productId = 1L,
                quantity = 100
            ),
            StockDomainFixture.create(
                stockId = 2L,
                productId = 2L,
                quantity = 50
            )
        )
        val orderProductQuantityCountMap = mapOf(
            1L to 2,
            2L to 1
        )

        val orderedProducts = OrderedProducts.create(products, stocks, orderProductQuantityCountMap)

        //when
        val result = orderedProducts.calculateTotalPrice()

        //then
        assertThat(result).isEqualTo(258100)
    }

    @DisplayName("주문 상품들의 재고가 충분하지 않으면 예외가 발생한다.")
    @Test
    fun isEnoughQuantity() {
        //given
        val products = listOf(
            ProductDomainFixture.create(
                productId = 1L,
                price = 129000
            ),
            ProductDomainFixture.create(
                productId = 2L,
                price = 100
            )
        )
        val stocks = listOf(
            StockDomainFixture.create(
                stockId = 1L,
                productId = 1L,
                quantity = 1
            ),
            StockDomainFixture.create(
                stockId = 2L,
                productId = 2L,
                quantity = 1
            )
        )
        val orderProductQuantityCountMap = mapOf(
            1L to 200,
            2L to 100
        )

        val orderedProducts = OrderedProducts.create(products, stocks, orderProductQuantityCountMap)

        //when //then
        assertThatThrownBy { orderedProducts.isEnoughQuantity() }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("주문한 상품 재고가 부족합니다.")
    }

}