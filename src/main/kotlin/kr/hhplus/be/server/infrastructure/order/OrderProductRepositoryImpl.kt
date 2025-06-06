package kr.hhplus.be.server.infrastructure.order

import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.order.OrderProduct
import kr.hhplus.be.server.domain.order.OrderProductRepository
import org.springframework.stereotype.Repository

@Repository
class OrderProductRepositoryImpl(
    private val orderProductJpaRepository: OrderProductJpaRepository
) : OrderProductRepository {

    override fun saveAll(orderProducts: List<OrderProduct>) {
        orderProductJpaRepository.saveAll(orderProducts)
    }

    override fun findByOrderIn(orders: List<Order>): List<OrderProduct> {
        return orderProductJpaRepository.findByOrderIn(orders)
    }

}