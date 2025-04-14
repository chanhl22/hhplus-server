package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.fixture.payment.PaymentCommandFixture
import kr.hhplus.be.server.fixture.payment.PaymentDomainFixture
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any

@ExtendWith(MockitoExtension::class)
class PaymentServiceTest {

    @Mock
    private lateinit var paymentRepository: PaymentRepository

    @InjectMocks
    private lateinit var paymentService: PaymentService

    @DisplayName("결제를 처리한다.")
    @Test
    fun process() {
        //given
        val payment = PaymentDomainFixture.create()
        BDDMockito.given(paymentRepository.save(any()))
            .willReturn(payment)

        //when
        val paymentCommand = PaymentCommandFixture.create()
        paymentService.process(paymentCommand)

        //then
        Mockito.verify(paymentRepository, times(1))
            .save(any())
    }

}