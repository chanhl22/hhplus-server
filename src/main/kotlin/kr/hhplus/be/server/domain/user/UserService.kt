package kr.hhplus.be.server.domain.user

import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun find(userId: Long): User {
        return userRepository.find(userId)
    }

    fun findUserWithPointForOrder(userId: Long): User {
        val user = userRepository.findUserWithPoint(userId)
            ?: throw IllegalArgumentException("존재하지 않는 사용자입니다. userId = $userId")

//        user.validatePointUsable()

        return user
    }
}