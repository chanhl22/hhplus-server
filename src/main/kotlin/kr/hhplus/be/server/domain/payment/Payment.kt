package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.domain.order.Order

enum class PaymentStatus(
    val description: String
) {
    SUCCEEDED("결제 완료"),
    FAILED("결제 실패"),
    ;
}

class Payment(
    val id: Long? = null,
    val order: Order,
    val amount: Int,
    val paymentStatus: PaymentStatus
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
