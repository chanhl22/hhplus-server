package kr.hhplus.be.server.fixture.order

import kr.hhplus.be.server.application.order.OrderCriteria
import kr.hhplus.be.server.application.order.OrderResult
import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.order.OrderCommand
import kr.hhplus.be.server.domain.order.OrderEvent
import kr.hhplus.be.server.domain.order.OrderInfo
import kr.hhplus.be.server.domain.order.OrderPoint
import kr.hhplus.be.server.domain.order.OrderProduct
import kr.hhplus.be.server.domain.order.OrderStatus
import kr.hhplus.be.server.domain.order.OrderedProduct
import kr.hhplus.be.server.domain.order.OrderedProducts
import kr.hhplus.be.server.domain.order.coupon.EmptyOrderCoupon
import kr.hhplus.be.server.domain.order.coupon.OrderCoupon
import kr.hhplus.be.server.fixture.point.PointFixtureDefault.DEFAULT_BALANCE
import kr.hhplus.be.server.fixture.user.UserFixtureDefault.DEFAULT_USER_ID
import java.time.LocalDateTime

object OrderCriteriaFixture {
    fun create(
        userId: Long = DEFAULT_USER_ID,
        products: List<OrderCriteria.OrderProduct> = emptyList(),
        couponId: Long? = 1L
    ): OrderCriteria.Order {
        return OrderCriteria.Order(
            userId = userId,
            products = products,
            couponId = couponId
        )
    }

    fun create(
        productId: Long = 1L,
        quantity: Int = 5,
    ): OrderCriteria.OrderProduct {
        return OrderCriteria.OrderProduct(
            productId = productId,
            quantity = quantity,
        )
    }
}

object OrderResultFixture {
    fun create(
        orderId: Long = 1L
    ): OrderResult.Order {
        return OrderResult.Order(
            orderId = orderId
        )
    }
}

object OrderDomainFixture {
    fun create(
        orderId: Long = 1L,
        userId: Long = DEFAULT_USER_ID,
        totalPrice: Int = 100000,
        couponId: Long? = 1L,
        registeredAt: LocalDateTime = LocalDateTime.now(),
        status: OrderStatus = OrderStatus.PENDING,
        orderProducts: List<OrderProduct> = emptyList()
    ): Order {
        val order = Order(
            id = orderId,
            userId = userId,
            totalPrice = totalPrice,
            couponId = couponId,
            registeredAt = registeredAt,
            status = status,
        )
        orderProducts.map { orderProduct -> order.orderProducts.add(orderProduct) }
        return order
    }
}

object OrderProductDomainFixture {
    fun create(
        order: Order = OrderDomainFixture.create(),
        productId: Long = 1L,
        quantity: Int = 10
    ): OrderProduct {
        return OrderProduct(
            order = order,
            productId = productId,
            quantity = quantity
        )
    }
}

object OrderInfoFixture {
    fun create(
        productId: Long = 1L,
        totalSales: Int = 10000
    ): OrderInfo.ProductStatistics {
        return OrderInfo.ProductStatistics(
            productId = productId,
            totalSales = totalSales
        )
    }
}

object OrderCommandFixture {
    fun create(
        userId: Long = 100L,
        pointId: Long = 10L,
        products: List<OrderCommand.OrderProduct> = listOf(
            OrderCommand.OrderProduct(productId = 1L, quantity = 2),
            OrderCommand.OrderProduct(productId = 2L, quantity = 1)
        ),
        couponId: Long? = 1L
    ): OrderCommand.Create {
        return OrderCommand.Create(
            userId = userId,
            pointId = pointId,
            products = products,
            couponId = couponId
        )
    }
}

object OrderPointFixture {
    fun create(
        userId: Long = DEFAULT_USER_ID,
        balance: Int = DEFAULT_BALANCE
    ): OrderPoint {
        return OrderPoint(
            userId = userId,
            balance = balance
        )
    }
}

object OrderedProductsFixture {
    fun create(
        products: List<OrderedProduct> = listOf(
            OrderedProduct(1L, "상품1", 10000, 10),
            OrderedProduct(2L, "상품2", 20000, 20)
        ),
        orderProductQuantityCountMap: Map<Long, Int> = mapOf(
            1L to 2,
            2L to 1
        )
    ): OrderedProducts {
        return OrderedProducts(
            products = products,
            orderProductQuantityCountMap = orderProductQuantityCountMap
        )
    }
}

object OrderCouponFixture {
    fun create(
    ): OrderCoupon {
        return EmptyOrderCoupon
    }
}

object OrderEventFixture {
    fun create(
        orderId: Long = 1L,
        userId: Long = 100L,
        pointId: Long = 10L,
        products: List<Pair<Long, Int>> = listOf(1L to 2, 2L to 1),
        couponId: Long? = 1L,
        totalPrice: Int = 18000,
        productsDetail: List<OrderEvent.OrderedProduct> = listOf(
            OrderEvent.OrderedProduct(
                productId = 1L,
                name = "상품A",
                price = 5000,
                quantity = 2
            ),
            OrderEvent.OrderedProduct(
                productId = 2L,
                name = "상품B",
                price = 8000,
                quantity = 1
            )
        ),
        discountType: String? = "AMOUNT",
        discountValue: Int? = 1000
    ): OrderEvent.Completed {
        return OrderEvent.Completed(
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