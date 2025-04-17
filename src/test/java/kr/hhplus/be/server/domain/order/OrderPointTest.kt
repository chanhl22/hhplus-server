package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.fixture.point.PointDomainFixture
import kr.hhplus.be.server.fixture.user.UserDomainFixture
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class OrderPointTest {

    @DisplayName("주문에 사용할 포인트를 생성한다.")
    @Test
    fun create() {
        //given
        val user = UserDomainFixture.create(userId = 1L)
        val point = PointDomainFixture.create(balance = 10000)

        //when
        val result = OrderPoint.create(user, point)

        //then
        assertThat(result)
            .extracting("userId", "balance")
            .containsExactly(
                1L, 10000
            )
    }

    @DisplayName("주문 상품들의 재고가 충분하지 않으면 예외가 발생한다.")
    @Test
    fun isEnoughQuantity() {
        //given
        val user = UserDomainFixture.create(userId = 1L)
        val point = PointDomainFixture.create(balance = 10000)

        val orderPoint = OrderPoint.create(user, point)

        //when //then
        assertThatThrownBy { orderPoint.isEnoughBalance(900000) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("충전된 포인트가 부족합니다.")
    }

}