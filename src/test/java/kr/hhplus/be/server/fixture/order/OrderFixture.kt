package kr.hhplus.be.server.fixture.order

import kr.hhplus.be.server.application.order.OrderCriteria
import kr.hhplus.be.server.application.order.OrderResult
import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.order.OrderCommand
import kr.hhplus.be.server.domain.order.OrderInfo
import kr.hhplus.be.server.domain.order.OrderPoint
import kr.hhplus.be.server.domain.order.OrderProduct
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
}

object OrderResultFixture {
    fun create(
        orderId: Long = 1L,
        totalAmount: Int = 10000,
        paymentId: Long = 1L,
        remainBalance: Int = 10000,
    ): OrderResult.Order {
        return OrderResult.Order(
            orderId = orderId,
            totalAmount = totalAmount,
            paymentId = paymentId,
            remainBalance = remainBalance,
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
        orderProducts: List<OrderProduct> = emptyList()
    ): Order {
        val order = Order(
            id = orderId,
            userId = userId,
            totalPrice = totalPrice,
            couponId = couponId,
            registeredAt = registeredAt
        )
        orderProducts.map { orderProduct -> order.orderProducts.add(orderProduct) }
        return order
    }
}

object OrderProductDomainFixture {
    fun create(
        order: Order = OrderDomainFixture.create(),
        productId: Long = 1L,
    ): OrderProduct {
        return OrderProduct(
            order = order,
            productId = productId,
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
        orderPoint: OrderPoint = OrderPointFixture.create(),
        orderedProducts: OrderedProducts = OrderedProductsFixture.create(),
        orderCoupon: OrderCoupon = OrderCouponFixture.create()
    ): OrderCommand.Order {
        return OrderCommand.Order(
            orderPoint = orderPoint,
            orderedProducts = orderedProducts,
            orderCoupon = orderCoupon
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
            OrderedProduct(1L, 10000, 10),
            OrderedProduct(2L, 20000, 20)
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