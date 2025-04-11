package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.user.User
import java.time.LocalDateTime

enum class DiscountType(
    val description: String
) {
    PERCENT("퍼센트 할인"),
    AMOUNT("금액 할인")
}

class Coupon(
    val id: Long = 0L,
    val user: User,
    val discountType: DiscountType,
    val discountValue: Int,
    val expiresAt: LocalDateTime
)
