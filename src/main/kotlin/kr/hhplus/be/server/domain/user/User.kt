package kr.hhplus.be.server.domain.user

import kr.hhplus.be.server.domain.point.Point

class User(
    val id: Long,
    val name: String,
    val point: Point
) {
    fun validatePointUsable() {
        if (isEmptyBalance()) {
            throw IllegalArgumentException("충전된 금액이 없습니다.")
        }
    }

    private fun isEmptyBalance() = point.balance <= 0
}
