package kr.hhplus.be.server.domain.point

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
class PointServiceTest {

    @Mock
    private lateinit var pointRepository: PointRepository

    @DisplayName("유저가 가지고 있는 포인트를 조회한다.")
    @Test
    fun find() {
        //given
        val pointService = PointService(pointRepository)

        val point = Point(1L, 10000)
        BDDMockito.given(pointRepository.find(ArgumentMatchers.anyLong()))
            .willReturn(point)

        //when
        val result = pointService.find(1)

        //then
        assertThat(result)
            .extracting("id", "amount")
            .containsExactly(point.id, point.amount)
        Mockito.verify(pointRepository, times(1))
            .find(anyLong())
    }

}