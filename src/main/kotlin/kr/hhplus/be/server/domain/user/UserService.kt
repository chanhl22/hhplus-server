package kr.hhplus.be.server.domain.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository
) {
    fun find(userId: Long): User {
        return userRepository.find(userId)
    }

}