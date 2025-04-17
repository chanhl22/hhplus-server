package kr.hhplus.be.server.domain.coupon

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class UserCoupon(
    val userId: Long,

    var couponId: Long? = null,

    val isUsed: Boolean = false,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) {
    companion object {
        fun create(
            userId: Long,
            couponId: Long
        ): UserCoupon {
            val userCoupon = UserCoupon(userId = userId, couponId = couponId)
            return userCoupon
        }
    }

    fun used(): UserCoupon {
        return UserCoupon(userId, couponId, true, id)
    }

}
