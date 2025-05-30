package kr.hhplus.be.server.fixture.point

import kr.hhplus.be.server.application.point.PointCriteria
import kr.hhplus.be.server.application.point.PointResult
import kr.hhplus.be.server.domain.point.Point
import kr.hhplus.be.server.domain.point.PointCommand
import kr.hhplus.be.server.fixture.point.PointFixtureDefault.DEFAULT_BALANCE
import kr.hhplus.be.server.fixture.point.PointFixtureDefault.DEFAULT_POINT_ID
import kr.hhplus.be.server.fixture.user.UserFixtureDefault.DEFAULT_USER_ID

object PointFixtureDefault {
    const val DEFAULT_POINT_ID = 1L
    const val DEFAULT_BALANCE = 100_000
}

object PointCriteriaFixture {
    fun createCharge(
        userId: Long = DEFAULT_USER_ID,
        amount: Int = 10000
    ): PointCriteria.Charge {
        return PointCriteria.Charge(
            userId = userId,
            amount = amount
        )
    }
}

object PointResultFixture {
    fun createFind(
        userId: Long = DEFAULT_USER_ID,
        balance: Int = 100000
    ): PointResult.Find {
        return PointResult.Find(
            userId = userId,
            balance = balance
        )
    }

    fun createCharge(
        userId: Long = DEFAULT_USER_ID,
        chargedAmount: Int = 100000,
        balance: Int = 110000
    ): PointResult.Charge {
        return PointResult.Charge(
            userId = userId,
            chargedAmount = chargedAmount,
            balance = balance
        )
    }
}

object PointCommandFixture {
    fun create(
        orderId: Long = 1L,
        userId: Long = 100L,
        pointId: Long = 10L,
        products: List<Pair<Long, Int>> = listOf(1L to 2, 2L to 1),
        couponId: Long? = 1L,
        totalPrice: Int = 30000,
        discountType: String? = "AMOUNT",
        discountValue: Int? = 1000,
        productsDetail: List<PointCommand.OrderedProduct> = listOf(
            PointCommand.OrderedProduct(
                productId = 1L,
                name = "상품 A",
                price = 10000,
                quantity = 2
            ),
            PointCommand.OrderedProduct(
                productId = 2L,
                name = "상품 B",
                price = 10000,
                quantity = 1
            )
        )
    ): PointCommand.Deduct {
        return PointCommand.Deduct(
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

object PointDomainFixture {
    fun create(
        pointId: Long = DEFAULT_POINT_ID,
        balance: Int = DEFAULT_BALANCE
    ): Point {
        return Point(
            id = pointId,
            balance = balance
        )
    }
}