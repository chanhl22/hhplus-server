package kr.hhplus.be.server.infrastructure.user

import kr.hhplus.be.server.domain.user.User
import kr.hhplus.be.server.domain.user.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : UserRepository {

    override fun find(userId: Long): User {
        return userJpaRepository.findById(userId)
            .orElseThrow(::IllegalArgumentException)
    }

}