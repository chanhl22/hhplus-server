package kr.hhplus.be.server.domain.point

class Point(
    val id: Long,
    var balance: Int
) {
    fun charge(amount: Int) {
        balance += amount
    }
}
