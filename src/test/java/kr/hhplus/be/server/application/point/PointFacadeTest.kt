package kr.hhplus.be.server.application.point

import kr.hhplus.be.server.domain.point.Point
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.user.User
import kr.hhplus.be.server.domain.user.UserService
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
class PointFacadeTest {

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var pointService: PointService

    @DisplayName("상품을 조회한다.")
    @Test
    fun find() {
        //given
        val pointFacade = PointFacade(pointService, userService)

        val user = User(1L, "이찬희B", Point(1L, 100000))
        val point = Point(1L, 10000)
        BDDMockito.given(userService.find(ArgumentMatchers.anyLong()))
            .willReturn(user)
        BDDMockito.given(pointService.find(ArgumentMatchers.anyLong()))
            .willReturn(point)

        //when
        val result = pointFacade.find(PointCriteria(1L))

        //then
        assertThat(result)
            .extracting("user.id", "point.amount")
            .containsExactly(user.id, point.amount)
        Mockito.verify(userService, times(1))
            .find(anyLong())
        Mockito.verify(pointService, times(1))
            .find(anyLong())
    }

}