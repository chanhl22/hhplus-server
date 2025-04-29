package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.fixture.point.PointDomainFixture
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any

@ExtendWith(MockitoExtension::class)
class PointServiceTest {

    @Mock
    private lateinit var pointRepository: PointRepository

    @InjectMocks
    private lateinit var pointService: PointService

    @DisplayName("유저가 가지고 있는 포인트를 조회한다.")
    @Test
    fun find() {
        //given
        val point = PointDomainFixture.create()
        BDDMockito.given(pointRepository.find(any()))
            .willReturn(point)

        //when
        pointService.find(1)

        //then
        Mockito.verify(pointRepository, times(1))
            .find(any())
    }

    @DisplayName("유저의 포인트를 충전한다.")
    @Test
    fun charge() {
        //given
        val point = PointDomainFixture.create()
        BDDMockito.given(pointRepository.findWithPessimisticLock(any()))
            .willReturn(point)

        val updatedPoint = PointDomainFixture.create(balance = 110000)
        BDDMockito.given(pointRepository.save(any()))
            .willReturn(updatedPoint)

        //when
        pointService.charge(1L, 10000)

        //then
        Mockito.verify(pointRepository, times(1))
            .findWithPessimisticLock(any())
        Mockito.verify(pointRepository, times(1))
            .save(any())
    }

    @DisplayName("유저의 포인트를 차감한다.")
    @Test
    fun use() {
        //given
        val point = PointDomainFixture.create()
        BDDMockito.given(pointRepository.findWithPessimisticLock(any()))
            .willReturn(point)

        val updatedPoint = PointDomainFixture.create(balance = 90000)
        BDDMockito.given(pointRepository.save(any()))
            .willReturn(updatedPoint)

        //when
        pointService.use(1L, 10000)

        //then
        Mockito.verify(pointRepository, times(1))
            .findWithPessimisticLock(any())
        Mockito.verify(pointRepository, times(1))
            .save(any())
    }

}