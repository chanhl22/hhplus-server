package kr.hhplus.be.server.domain.coupon

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

enum class DiscountType(
    val description: String
) {
    PERCENT("퍼센트 할인"),
    AMOUNT("금액 할인")
}

@Entity
@Table(name = "coupon")
class Coupon(
    val name: String,

    @Enumerated(EnumType.STRING)
    val discountType: DiscountType,

    val discountValue: Int,

    var remainingQuantity: Int,

    val expiredAt: LocalDateTime,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
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

    fun issueTo(userId: Long): UserCoupon {
        return UserCoupon.create(userId, this.id)
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
