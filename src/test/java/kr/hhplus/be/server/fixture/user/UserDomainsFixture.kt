package kr.hhplus.be.server.fixture.user

import kr.hhplus.be.server.domain.point.Point
import kr.hhplus.be.server.domain.user.User

object UserFixture {
    fun create(
        userId: Long = 1L,
        name: String = "이찬희B",
        pointId: Long = 1L,
        balance: Int = 100000
    ): User {
        return User(
            id = userId,
            name = name,
            point = Point(pointId, balance)
        )
    }

}