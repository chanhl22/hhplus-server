package kr.hhplus.be.server.domain.point

class PointCommand {
    data class Charge (
        val pointId: Long,
        val amount: Int
    )

    companion object {
        fun of(pointId: Long, amount: Int): Charge {
            return Charge(
                pointId = pointId,
                amount = amount
            )
        }
    }

}
