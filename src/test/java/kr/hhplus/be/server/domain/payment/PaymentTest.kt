package kr.hhplus.be.server.domain.payment

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

}