package kr.hhplus.be.server.fixture.platform

import kr.hhplus.be.server.domain.platform.PlatformCommand

object PlatformCommandFixture {
    fun create(
        orderId: Long = 1L,
        totalPrice: Int = 30000,
        products: List<PlatformCommand.OrderedProduct> = listOf(
            PlatformCommand.OrderedProduct(
                productId = 101L,
                name = "테스트 상품 1",
                price = 10000,
                quantity = 1
            ),
            PlatformCommand.OrderedProduct(
                productId = 102L,
                name = "테스트 상품 2",
                price = 20000,
                quantity = 1
            )
        ),
        userId: Long = 10L,
        name: String = "홍길동",
        couponId: Long? = 555L,
        discountType: String? = "금액 할인",
        discountValue: Int? = 5000
    ): PlatformCommand.Payload {
        return PlatformCommand.Payload(
            orderId = orderId,
            totalPrice = totalPrice,
            products = products,
            userId = userId,
            name = name,
            couponId = couponId,
            discountType = discountType,
            discountValue = discountValue
        )
    }
}