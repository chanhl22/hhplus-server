package kr.hhplus.be.server.interfaces.point

import kr.hhplus.be.server.application.point.PointResult

class PointResponse {
    data class Find(
        val userId: Long,
        val balance: Int
    )

    data class Charge(
        val userId: Long,
        val chargedAmount: Int,
        val balance: Int
    )

    companion object {
        fun from(result: PointResult.Find): Find {
            return Find(
                userId = result.userId,
                balance = result.balance
            )
        }

        fun from(result: PointResult.Charge): Charge {
            return Charge(
                userId = result.userId,
                chargedAmount = result.chargedAmount,
                balance = result.balance
            )
        }
    }

}