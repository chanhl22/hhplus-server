package kr.hhplus.be.server.domain.user

interface UserRepository {

    fun find(userId: Long): User

}