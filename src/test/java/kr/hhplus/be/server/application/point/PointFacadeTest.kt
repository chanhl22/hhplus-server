package kr.hhplus.be.server.application.point

import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.user.UserService
import kr.hhplus.be.server.fixture.point.PointCriteriaFixture
import kr.hhplus.be.server.fixture.point.PointDomainFixture
import kr.hhplus.be.server.fixture.user.UserDomainFixture
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
class PointFacadeTest {

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var pointService: PointService

    @InjectMocks
    private lateinit var pointFacade: PointFacade

    @DisplayName("유저가 가지고 있는 포인트를 조회한다.")
    @Test
    fun find() {
        //given
        val user = UserDomainFixture.create()
        BDDMockito.given(userService.find(any()))
            .willReturn(user)

        val point = PointDomainFixture.create()
        BDDMockito.given(pointService.find(any()))
            .willReturn(point)

        //when
        pointFacade.find(1L)

        //then
        Mockito.verify(userService, times(1))
            .find(any())
        Mockito.verify(pointService, times(1))
            .find(any())
    }

    @DisplayName("유저의 포인트를 충전한다.")
    @Test
    fun charge() {
        //given
        val user = UserDomainFixture.create()
        BDDMockito.given(userService.find(any()))
            .willReturn(user)

        val updatedPoint = PointDomainFixture.create(balance = 110000)
        BDDMockito.given(pointService.charge(any()))
            .willReturn(updatedPoint)

        //when
        val criteria = PointCriteriaFixture.createCharge()
        pointFacade.charge(criteria)

        //then
        Mockito.verify(userService, times(1))
            .find(any())
        Mockito.verify(pointService, times(1))
            .charge(any())
    }

}