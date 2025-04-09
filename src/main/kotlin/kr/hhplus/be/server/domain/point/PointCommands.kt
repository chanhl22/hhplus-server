package kr.hhplus.be.server.domain.point

class PointCommands {
    data class PointCommand (
        val pointId: Long,
        val amount: Int
    ) {
        companion object {
            fun of(userId: Long, amount: Int): PointCommand {
                return PointCommand(
                    pointId = userId,
                    amount = amount
                )
            }
        }
    }
}
