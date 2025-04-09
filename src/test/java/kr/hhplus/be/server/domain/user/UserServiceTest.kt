package kr.hhplus.be.server.domain.user

import kr.hhplus.be.server.domain.point.Point
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
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @DisplayName("유저가 가지고 있는 포인트를 조회한다.")
    @Test
    fun find() {
        //given
        val userService = UserService(userRepository)

        val user = User(1L, "이찬희B", Point(1L, 100000))
        BDDMockito.given(userRepository.find(ArgumentMatchers.anyLong()))
            .willReturn(user)

        //when
        val result = userService.find(1)

        //then
        assertThat(result)
            .extracting("id", "name", "point.balance")
            .containsExactly(user.id, user.name, user.point.balance)
        Mockito.verify(userRepository, times(1))
            .find(anyLong())
    }

}