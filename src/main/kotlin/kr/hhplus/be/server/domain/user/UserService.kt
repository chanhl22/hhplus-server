package kr.hhplus.be.server.domain.user

import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun find(userId: Long): User {
        return userRepository.find(userId)
    }
}