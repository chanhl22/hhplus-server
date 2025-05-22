package kr.hhplus.be.server.domain.payment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PaymentTest {

    @Test
    @DisplayName("성공 상태의 Payment 객체를 생성한다")
    fun createPayment() {
        // given
        val orderId = 123L
        val amount = 5000

        // when
        val payment = Payment.create(orderId, amount)

        // then
        assertThat(payment.orderId).isEqualTo(orderId)
        assertThat(payment.amount).isEqualTo(amount)
        assertThat(payment.paymentStatus).isEqualTo(PaymentStatus.SUCCEEDED)
        assertThat(payment.id).isEqualTo(0L)
    }

}