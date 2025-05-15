package kr.hhplus.be.server.domain.coupon

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "coupon")
class Coupon(
    val name: String,

    @Enumerated(EnumType.STRING)
    val discountType: DiscountType,

    val discountValue: Int,

    val quantity: Int,

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

        val deductQuantity = quantity - 1
        return Coupon(name, discountType, discountValue, deductQuantity, expiredAt, id)
    }

    fun issueTo(userId: Long): UserCoupon {
        return UserCoupon.create(userId, this.id)
    }

    private fun isSoldOut(): Boolean {
        return this.quantity <= 0
    }

    private fun isExpired(): Boolean {
        return expiredAt.isBefore(LocalDateTime.now())
    }

}

enum class DiscountType(
    val description: String
) {
    PERCENT("퍼센트 할인"),
    AMOUNT("금액 할인")
}

enum class CouponReserveStatus(
    val code: Int,
    val description: String
) {

    SUCCESS(1, "성공"),
    ALREADY_REQUESTED(0, "이미 요청한 유저"),
    OUT_OF_STOCK(-1, "재고 부족"),
    NO_STOCK_INFO(-2, "재고 정보 없음")
    ;

    companion object {
        private val CODE_MAP = entries.associateBy { it.code }

        fun from(code: Int): CouponReserveStatus {
            return CODE_MAP[code] ?: throw IllegalStateException("There is no code.: $code")
        }
    }

}