package kr.hhplus.be.server.fixture.point

import kr.hhplus.be.server.application.point.PointResults
import kr.hhplus.be.server.domain.point.Point
import kr.hhplus.be.server.domain.user.User

object PointResultFixture {
    fun create(
        userId: Long = 1L,
        username: String = "이찬희B",
        pointId: Long = 1L,
        balance: Int = 100000
    ): PointResults.PointResult {
        return PointResults.PointResult(
            user = User(
                id = userId,
                name = username,
                point = Point(
                    id = pointId,
                    balance = balance
                )
            ),
            point = Point(
                id = pointId,
                balance = balance
            )
        )
    }
}

object ChargePointResultFixture {
    fun create(
        userId: Long = 1L,
        username: String = "이찬희B",
        pointId: Long = 1L,
        balance: Int = 100000
    ): PointResults.ChargePointResult {
        return PointResults.ChargePointResult(
            user = User(
                id = userId,
                name = username,
                point = Point(
                    id = pointId,
                    balance = balance
                )
            ),
            point = Point(
                id = pointId,
                balance = balance
            )
        )
    }
}