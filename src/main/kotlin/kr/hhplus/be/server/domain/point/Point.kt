package kr.hhplus.be.server.domain.point

class Point(
    val id: Long,
    var balance: Int
) {
    companion object {
        private const val MAX_POINT = 1000000000L
    }

    fun deduct(amount: Int) {
        if (isPointLessThan(amount)) {
            throw IllegalArgumentException("차감할 포인트가 없습니다.")
        }
        balance -= amount
    }

    fun charge(amount: Int) {
        if (isExceedMaxPoint(amount)) {
            throw IllegalArgumentException("포인트를 더 충전할 수 없습니다.")
        }
        balance += amount
    }

    private fun isPointLessThan(amount: Int) = balance < amount

    private fun isExceedMaxPoint(amount: Int) = balance + amount > MAX_POINT
}
