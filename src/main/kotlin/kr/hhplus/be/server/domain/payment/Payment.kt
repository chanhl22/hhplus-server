package kr.hhplus.be.server.domain.payment

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

enum class PaymentStatus(
    val description: String
) {
    SUCCEEDED("결제 완료"),
    FAILED("결제 실패"),
    ;
}

@Entity
@Table(name = "payment")
class Payment(

    val orderId: Long,

    val amount: Int,

    val paymentStatus: PaymentStatus,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) {
    companion object {
        fun create(
            orderId: Long,
            amount: Int
        ): Payment {
            return Payment(
                orderId = orderId,
                amount = amount,
                paymentStatus = PaymentStatus.SUCCEEDED
            )
        }
    }

}
