package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.fixture.payment.PaymentCommandFixture
import kr.hhplus.be.server.infrastructure.payment.PaymentJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class PaymentServiceIntegrationTest {

    @Autowired
    private lateinit var paymentService: PaymentService

    @MockitoBean
    private lateinit var paymentEventPublisher: PaymentEventPublisher

    @Autowired
    private lateinit var paymentJpaRepository: PaymentJpaRepository

    @DisplayName("주문서를 바탕으로 결제 정보를 저장한다.")
    @Test
    fun process() {
        //given
        val command = PaymentCommandFixture.create(totalPrice = 10000)

        //when
        val result = paymentService.process(command)

        //then
        val findPayment = paymentJpaRepository.findById(result)
        assertThat(findPayment.get())
            .extracting("amount", "paymentStatus")
            .containsExactly(10000, PaymentStatus.SUCCEEDED)
    }

}