package kr.hhplus.be.server.domain.point

class PointCommands {
    data class PointCommand (
        val pointId: Long,
        val amount: Int
    ) {
        companion object {
            fun of(pointId: Long, amount: Int): PointCommand {
                return PointCommand(
                    pointId = pointId,
                    amount = amount
                )
            }
        }
    }
}
