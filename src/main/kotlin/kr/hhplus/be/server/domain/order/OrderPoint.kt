package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.point.Point
import kr.hhplus.be.server.domain.user.User

class OrderPoint(
    val userId: Long,
    val balance: Int
) {
    companion object {
        fun create(user: User, point: Point): OrderPoint {
            point.validateBalance()

            return OrderPoint(
                userId = user.id,
                balance = point.balance
            )
        }
    }

    fun isEnoughBalance(totalPrice: Int) {
        if (balance < totalPrice) {
            throw IllegalArgumentException("충전된 포인트가 부족합니다.")
        }
    }

}
