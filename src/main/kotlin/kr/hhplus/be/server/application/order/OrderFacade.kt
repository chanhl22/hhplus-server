package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.common.annotation.DistributedLock
import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.order.OrderCommand
import kr.hhplus.be.server.domain.order.OrderEvent
import kr.hhplus.be.server.domain.order.OrderEventPublisher
import kr.hhplus.be.server.domain.order.OrderPoint
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.order.OrderedProducts
import kr.hhplus.be.server.domain.order.coupon.OrderCoupon
import kr.hhplus.be.server.domain.payment.PaymentCommand
import kr.hhplus.be.server.domain.payment.PaymentService
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.rank.ProductRankingCommand
import kr.hhplus.be.server.domain.rank.ProductRankingService
import kr.hhplus.be.server.domain.user.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager

@Service
@Transactional(readOnly = true)
class OrderFacade(
    private val productService: ProductService,
    private val orderService: OrderService,
    private val userService: UserService,
    private val paymentService: PaymentService,
    private val pointService: PointService,
    private val couponService: CouponService,
    private val productRankingService: ProductRankingService,
    private val orderEventPublisher: OrderEventPublisher
) {

    @DistributedLock(key = "#criteria.toLockKeys()")
    @Transactional
    fun order(criteria: OrderCriteria.Order): OrderResult.Order {
        val user = userService.find(criteria.userId)
        val point = pointService.find(user.pointId)

        val productInfo = productService.findAll(criteria.getProductIds())

        val coupon = couponService.find(criteria.couponId, user.id)

        val orderPoint = OrderPoint.create(user, point)
        val orderedProducts = OrderedProducts.create(
            productInfo.products,
            productInfo.stocks,
            criteria.createOrderProductQuantityCountMap()
        )
        val orderCoupon = OrderCoupon.from(coupon)
        val order = orderService.order(OrderCommand.of(orderPoint, orderedProducts, orderCoupon))

        couponService.isUsed(order.couponId, user.id)
        productService.deduct(criteria.toDeduct())
        val payment = paymentService.process(PaymentCommand.of(point, order))
        if (payment.isSuccess()) {
            pointService.use(point.id, order.totalPrice)
        }

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {
                override fun afterCommit() {
                    productRankingService.upsertRanking(
                        ProductRankingCommand.of(orderedProducts.createProductIdToNameAndQuantityMap())
                    )
                }
            })
        }

        orderEventPublisher.publish(OrderEvent.of(order, orderedProducts, user, coupon))

        return OrderResult.of(point, order, payment)
    }

}