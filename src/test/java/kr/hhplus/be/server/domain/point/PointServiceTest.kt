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
            .extracting("id", "balance")
            .containsExactly(point.id, point.balance)
        Mockito.verify(pointRepository, times(1))
            .find(anyLong())
    }

    @DisplayName("유저의 포인트를 충전한다.")
    @Test
    fun charge() {
        //given
        val pointService = PointService(pointRepository)

        val point = Point(1L, 100000)
        BDDMockito.given(pointRepository.find(ArgumentMatchers.anyLong()))
            .willReturn(point)

        val updatedPoint = Point(1L, 110000)
        BDDMockito.given(pointRepository.update(point))
            .willReturn(updatedPoint)

        //when
        val command = PointCommands.PointCommand.of(1L, 10000)
        val result = pointService.charge(command)

        //then
        assertThat(result)
            .extracting("id", "balance")
            .containsExactly(updatedPoint.id, updatedPoint.balance)
        Mockito.verify(pointRepository, times(1))
            .find(anyLong())
        Mockito.verify(pointRepository, times(1))
            .update(point)
    }

    @DisplayName("유저의 포인트를 차감한다.")
    @Test
    fun pay() {
        //given
        val pointService = PointService(pointRepository)

        val point = Point(1L, 100000)
        BDDMockito.given(pointRepository.find(ArgumentMatchers.anyLong()))
            .willReturn(point)

        val updatedPoint = Point(1L, 110000)
        BDDMockito.given(pointRepository.update(point))
            .willReturn(updatedPoint)

        //when
        val result = pointService.pay(1L, 10000)

        //then
        assertThat(result)
            .extracting("id", "balance")
            .containsExactly(updatedPoint.id, updatedPoint.balance)
        Mockito.verify(pointRepository, times(1))
            .find(anyLong())
        Mockito.verify(pointRepository, times(1))
            .update(point)
    }

}