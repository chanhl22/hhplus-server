package kr.hhplus.be.server.application.point

import kr.hhplus.be.server.domain.point.Point
import kr.hhplus.be.server.domain.user.User

data class PointResult (
    val user: User,
    val point: Point
) {
    companion object {
        fun of(user: User, point: Point): PointResult {
            return PointResult(
                user = user,
                point = point
            )
        }
    }
}
