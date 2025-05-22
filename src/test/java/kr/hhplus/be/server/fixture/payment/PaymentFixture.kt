package kr.hhplus.be.server.fixture.payment

import kr.hhplus.be.server.domain.payment.Payment
import kr.hhplus.be.server.domain.payment.PaymentCommand
import kr.hhplus.be.server.domain.payment.PaymentStatus

object PaymentCommandFixture {
    fun create(
        orderId: Long = 1L,
        userId: Long = 100L,
        pointId: Long = 10L,
        products: List<Pair<Long, Int>> = listOf(1L to 2, 2L to 1),
        couponId: Long? = 1L,
        totalPrice: Int = 30000,
        discountType: String? = "AMOUNT",
        discountValue: Int? = 1000,
        productsDetail: List<PaymentCommand.OrderedProduct> = listOf(
            PaymentCommand.OrderedProduct(
                productId = 1L,
                name = "상품 A",
                price = 10000,
                quantity = 2
            ),
            PaymentCommand.OrderedProduct(
                productId = 2L,
                name = "상품 B",
                price = 10000,
                quantity = 1
            )
        )
    ): PaymentCommand.Save {
        return PaymentCommand.Save(
            orderId = orderId,
            userId = userId,
            pointId = pointId,
            products = products,
            couponId = couponId,
            totalPrice = totalPrice,
            productsDetail = productsDetail,
            discountType = discountType,
            discountValue = discountValue
        )
    }
}

object PaymentDomainFixture {
    fun create(
        paymentId: Long = 1L,
        orderId: Long = 1L,
        amount: Int = 100,
        paymentStatus: PaymentStatus = PaymentStatus.SUCCEEDED
    ): Payment {
        return Payment(
            id = paymentId,
            orderId = orderId,
            amount = amount,
            paymentStatus = paymentStatus
        )
    }

}