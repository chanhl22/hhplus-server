package kr.hhplus.be.server.application.point

import kr.hhplus.be.server.domain.point.PointCommand

class PointCriteria {
    data class Charge(
        val userId: Long,
        val amount: Int
    ) {
        fun toCommand(pointId: Long): PointCommand.Charge {
            return PointCommand.of(pointId, amount)
        }
    }

    companion object {
        fun of(userId: Long, amount: Int): Charge {
            return Charge(
                userId = userId,
                amount = amount
            )
        }
    }

}
