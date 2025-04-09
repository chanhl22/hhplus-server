package kr.hhplus.be.server.domain.point

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PointTest {

    @DisplayName("유저가 충전한 포인트 만큼 증가시킨다.")
    @Test
    fun increasePoint() {
        //given
        val amount = 10000
        val point = Point(1, 100000)

        //when
        point.charge(amount)

        //then
        assertThat(point.balance).isEqualTo(110000)
    }

}