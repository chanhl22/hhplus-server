package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.fixture.point.PointDomainFixture
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PointTest {

    @DisplayName("유저가 충전한 포인트 만큼 증가시킨다.")
    @Test
    fun increasePoint() {
        //given
        val amount = 10000
        val point = PointDomainFixture.create(1L, 100000)

        //when
        val result = point.charge(amount)

        //then
        assertThat(result.balance).isEqualTo(110000)
    }

    @DisplayName("포인트를 충전할 때 1,000,000,000을 초과하는 경우 예외가 발생한다.")
    @Test
    fun increasePoint2() {
        //given
        val amount = 1
        val point = PointDomainFixture.create(1L, 1000000000)

        //when //then
        assertThatThrownBy { point.charge(amount) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("포인트를 더 충전할 수 없습니다.")
    }

    @DisplayName("유저가 사용한 포인트 만큼 차감할 수 있다.")
    @Test
    fun deductPoint() {
        //given
        val amount = 1000
        val point = PointDomainFixture.create(1L, 10000)

        //when
        val result = point.deduct(amount)

        //then
        assertThat(result.balance).isEqualTo(9000)
    }

    @DisplayName("가진 포인트보다 높은 수량으로 차감 시도하는 경우 예외가 발생한다.")
    @Test
    fun deductPoint2() {
        //given
        val amount = 10001
        val point = PointDomainFixture.create(1L, 10000)

        //when //then
        assertThatThrownBy { point.deduct(amount) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("차감할 포인트가 없습니다.")
    }

}