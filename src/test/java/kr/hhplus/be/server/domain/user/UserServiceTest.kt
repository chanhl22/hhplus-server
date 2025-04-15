package kr.hhplus.be.server.domain.user

import kr.hhplus.be.server.fixture.user.UserFixture
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
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserService

    @DisplayName("유저를 조회한다.")
    @Test
    fun find() {
        //given
        val user = UserFixture.create()
        BDDMockito.given(userRepository.find(any()))
            .willReturn(user)

        //when
        userService.find(1)

        //then
        Mockito.verify(userRepository, times(1))
            .find(any())
    }

//    @DisplayName("사용자와 가지고 있는 포인트를 조회한다.")
//    @Test
//    fun findUserWithPointForOrder() {
//        //given
//        val userService = UserService(userRepository)
//
//        val user = User(1L, "이찬희B", Point(1L, 100000))
//        BDDMockito.given(userRepository.findUserWithPoint(ArgumentMatchers.anyLong()))
//            .willReturn(user)
//
//        //when
//        val result = userService.findUserWithPointForOrder(1)
//
//        //then
//        assertThat(result)
//            .extracting("id", "name", "point.balance")
//            .containsExactly(user.id, user.name, user.point.balance)
//        Mockito.verify(userRepository, times(1))
//            .findUserWithPoint(anyLong())
//    }

}