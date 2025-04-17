package kr.hhplus.be.server.application.point

import kr.hhplus.be.server.domain.point.Point
import kr.hhplus.be.server.domain.user.User

class PointResult {
    data class Find (
        val userId: Long,
        val balance: Int
    )

    data class Charge (
        val userId: Long,
        val chargedAmount: Int,
        val balance: Int
    )

    companion object {
        fun of(user: User, point: Point): Find {
            return Find(
                userId = user.id,
                balance = point.balance
            )
        }

        fun of(user: User, point: Point, amount: Int): Charge {
            return Charge(
                userId = user.id,
                chargedAmount = amount,
                balance = point.balance
            )
        }
    }

}
