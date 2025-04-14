package kr.hhplus.be.server.application.point

import kr.hhplus.be.server.domain.point.PointCommands.PointCommand

class PointCriteria {
    data class ChargePointCriterion(
        val userId: Long,
        val amount: Int
    ) {
        companion object {
            fun of(userId: Long, amount: Int): ChargePointCriterion {
                return ChargePointCriterion(
                    userId = userId,
                    amount = amount
                )
            }
        }

        fun toCommand(pointId: Long): PointCommand {
            return PointCommand.of(pointId, amount)
        }
    }

}
