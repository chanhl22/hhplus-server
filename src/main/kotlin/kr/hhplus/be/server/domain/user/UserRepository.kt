package kr.hhplus.be.server.domain.user

import org.springframework.stereotype.Repository

@Repository
interface UserRepository {

    fun find(userId: Long): User

    fun findUserWithPoint(userId: Long): User?

}