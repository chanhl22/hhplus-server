package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.fixture.payment.PaymentCommandFixture
import kr.hhplus.be.server.fixture.product.ProductDomainFixture
import kr.hhplus.be.server.fixture.user.UserFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class PaymentServiceTest {

    @Mock
    private lateinit var paymentRepository: PaymentRepository

    @Mock
    private lateinit var paymentFactory: PaymentFactory

    @DisplayName("결제 정보를 저장한다.")
    @Test
    fun save() {
        //given
        val paymentService = PaymentService(paymentRepository, paymentFactory)

        val user = UserFixture.create()
        val products = ProductDomainFixture.createProducts()
        val order = Order.create(user, products)
        val payment = Payment.create(order)
        BDDMockito.given(paymentFactory.create(order))
            .willReturn(payment)
        BDDMockito.given(paymentRepository.save(payment))
            .willReturn(payment)

        //when
        val paymentCommand = PaymentCommandFixture.create(order)
        val result = paymentService.save(paymentCommand)

        //then
        assertThat(result)
            .extracting("id", "amount")
            .containsExactly(order.id, order.totalPrice)
        Mockito.verify(paymentFactory, times(1))
            .create(order)
        Mockito.verify(paymentRepository, times(1))
            .save(payment)
    }

}