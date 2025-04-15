package kr.hhplus.be.server.domain.point

class Point(
    val id: Long,
    val balance: Int
) {
    companion object {
        private const val MAX_POINT = 1000000000L
    }

    fun deduct(amount: Int): Point {
        if (isPointLessThan(amount)) {
            throw IllegalArgumentException("차감할 포인트가 없습니다.")
        }

        val deductedPoint = balance - amount
        return Point(id, deductedPoint)
    }

    fun charge(amount: Int): Point {
        if (isExceedMaxPoint(amount)) {
            throw IllegalArgumentException("포인트를 더 충전할 수 없습니다.")
        }

        val chargedPoint = balance + amount
        return Point(id, chargedPoint)
    }

    private fun isPointLessThan(amount: Int): Boolean {
        return balance < amount
    }

    private fun isExceedMaxPoint(amount: Int): Boolean {
        return balance + amount > MAX_POINT
    }

}
