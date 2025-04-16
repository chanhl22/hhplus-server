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
    val id: Long? = null,
    val name: String,
    val discountType: DiscountType,
    val discountValue: Int,
    var remainingQuantity: Int,
    val expiredAt: LocalDateTime
) {
    fun publish(): Coupon {
        if (isSoldOut()) {
            throw IllegalStateException("쿠폰이 모두 소진되었습니다.")
        }

        if (isExpired()) {
            throw IllegalStateException("쿠폰이 만료되었습니다.")
        }

        return this
    }

    fun deduct(): Coupon {
        if (isSoldOut()) {
            throw IllegalStateException("쿠폰이 모두 소진되었습니다.")
        }
        remainingQuantity -= 1
        return this
    }

    fun issueTo(user: User): UserCoupon {
        return UserCoupon.create(user, this)
    }

    fun validateRemainingQuantity() {
        if (remainingQuantity < 0) {
            throw IllegalArgumentException("남은 쿠폰의 수량은 0보다 작을 수 없습니다.")
        }
    }

    private fun isSoldOut(): Boolean {
        return this.remainingQuantity <= 0
    }

    private fun isExpired(): Boolean {
        return expiredAt.isBefore(LocalDateTime.now())
    }

}
