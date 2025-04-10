package kr.hhplus.be.server.fixture.user

import kr.hhplus.be.server.domain.point.Point
import kr.hhplus.be.server.domain.user.User

object UserFixture {
    fun create(
        userId: Long = 1L,
        name: String = "이찬희B",
    ): User {
        return User(
            id = userId,
            name = name,
            point = Point(1L, 100000)
        )
    }

}