package kr.hhplus.be.server.application.point

class PointCriteria {
    data class Charge(
        val userId: Long,
        val amount: Int
    )

    companion object {
        fun of(userId: Long, amount: Int): Charge {
            return Charge(
                userId = userId,
                amount = amount
            )
        }
    }

}
