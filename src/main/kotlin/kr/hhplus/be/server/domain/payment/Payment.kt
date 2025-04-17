package kr.hhplus.be.server.domain.payment

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import kr.hhplus.be.server.domain.order.Order

enum class PaymentStatus(
    val description: String
) {
    SUCCEEDED("결제 완료"),
    FAILED("결제 실패"),
    ;
}

@Entity
class Payment(

    @OneToOne
    val order: Order,

    val amount: Int,

    val paymentStatus: PaymentStatus,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) {
    companion object {
        fun decide(order: Order, userPointBalance: Int): Payment {
            return if (userPointBalance < order.totalPrice) {
                fail(order)
            } else {
                success(order)
            }
        }

        private fun success(
            order: Order,
            paymentStatus: PaymentStatus = PaymentStatus.SUCCEEDED
        ): Payment {
            return Payment(
                order = order,
                amount = order.totalPrice,
                paymentStatus = paymentStatus
            )
        }

        private fun fail(
            order: Order,
            paymentStatus: PaymentStatus = PaymentStatus.FAILED
        ): Payment {
            return Payment(
                order = order,
                amount = order.totalPrice,
                paymentStatus = paymentStatus
            )
        }
    }

    fun isSuccess(): Boolean {
        return this.paymentStatus == PaymentStatus.SUCCEEDED
    }

}
