package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.fixture.stock.StockDomainFixture
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class StockTest {

    @DisplayName("재고가 0보다 작다면 예외가 발생한다.")
    @Test
    fun validateQuantity() {
        //given
        val stock = StockDomainFixture.create(
            quantity = -1
        )

        //when //then
        assertThatThrownBy { stock.validateQuantity() }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("상품 재고는 0보다 작을 수 없습니다.")
    }

    @DisplayName("유저가 주문한 재고 만큼 감소시킨다.")
    @Test
    fun deduct() {
        //given
        val orderQuantity = 100
        val stock = StockDomainFixture.create(quantity = 100)

        //when
        val result = stock.deduct(orderQuantity)

        //then
        assertThat(result.quantity).isEqualTo(0)
    }

    @DisplayName("가진 재고보다 높은 수량으로 차감 시도하는 경우 예외가 발생한다.")
    @Test
    fun deduct2() {
        //given
        val orderQuantity = 101
        val stock = StockDomainFixture.create(quantity = 100)

        //when //then
        assertThatThrownBy { stock.deduct(orderQuantity) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("재고가 모두 소진되었습니다.")
    }

}