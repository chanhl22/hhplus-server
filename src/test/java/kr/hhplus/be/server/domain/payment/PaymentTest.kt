package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.fixture.order.OrderDomainFixture
import kr.hhplus.be.server.fixture.payment.PaymentDomainFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PaymentTest {

    @DisplayName("결제 프로세스가 성공했는지 확인한다.")
    @Test
    fun isSuccess() {
        //given
        val payment1 = PaymentDomainFixture.create(paymentStatus = PaymentStatus.SUCCEEDED)
        val payment2 = PaymentDomainFixture.create(paymentStatus = PaymentStatus.FAILED)

        //when
        val result1 = payment1.isSuccess()
        val result2 = payment2.isSuccess()

        //then
        assertThat(result1).isTrue()
        assertThat(result2).isFalse()
    }

    @DisplayName("결제 성공/실패를 결정한다.")
    @Test
    fun decide() {
        //given
        val order1 = OrderDomainFixture.create(totalPrice = 10000)
        val userPointBalance1 = 10000

        val order2 = OrderDomainFixture.create(totalPrice = 10001)
        val userPointBalance2 = 10000

        //when
        val result1 = Payment.decide(order1, userPointBalance1)
        val result2 = Payment.decide(order2, userPointBalance2)

        //then
        assertThat(result1.paymentStatus).isEqualTo(PaymentStatus.SUCCEEDED)
        assertThat(result2.paymentStatus).isEqualTo(PaymentStatus.FAILED)
    }

}