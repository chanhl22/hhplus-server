package kr.hhplus.be.server.domain.user

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
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserService

    @DisplayName("유저를 조회한다.")
    @Test
    fun find() {
        //given
        val user = UserDomainFixture.create()
        BDDMockito.given(userRepository.find(any()))
            .willReturn(user)

        //when
        userService.find(1)

        //then
        Mockito.verify(userRepository, times(1))
            .find(any())
    }

}