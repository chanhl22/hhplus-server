package kr.hhplus.be.server.fixture.user

import kr.hhplus.be.server.domain.user.User
import kr.hhplus.be.server.fixture.point.PointDomainFixture
import kr.hhplus.be.server.fixture.point.PointFixtureDefault.DEFAULT_BALANCE
import kr.hhplus.be.server.fixture.point.PointFixtureDefault.DEFAULT_POINT_ID
import kr.hhplus.be.server.fixture.user.UserFixtureDefault.DEFAULT_NAME
import kr.hhplus.be.server.fixture.user.UserFixtureDefault.DEFAULT_USER_ID

object UserFixtureDefault {
    const val DEFAULT_USER_ID = 1L
    const val DEFAULT_NAME = "이찬희B"
}

object UserDomainFixture {
    fun create(
        userId: Long = DEFAULT_USER_ID,
        name: String = DEFAULT_NAME,
        pointId: Long = DEFAULT_POINT_ID,
        balance: Int = DEFAULT_BALANCE
    ): User {
        val point = PointDomainFixture.create(pointId, balance)
        return User(
            id = userId,
            name = name,
            pointId = point.id
        )
    }

}