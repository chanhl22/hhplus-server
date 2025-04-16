package kr.hhplus.be.server.domain.user

import kr.hhplus.be.server.fixture.user.UserDomainFixture
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceIntegrationTest {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userJpaRepository: UserJpaRepository

    @DisplayName("유저를 조회한다.")
    @Test
    fun find() {
        //given
        val user = UserDomainFixture.create(userId = 0L, name = "이찬희B", pointId = 1L)
        val savedUser = userJpaRepository.save(user)

        //when
        val result = userService.find(savedUser.id)

        //then
        assertThat(result)
            .extracting("name", "pointId")
            .containsExactly("이찬희B", 1L)
    }
}