package kr.hhplus.be.server.domain.order

import org.springframework.stereotype.Repository

@Repository
interface OrderProductRepository {

    fun saveAll(orderProducts: List<OrderProduct>)

    fun findByOrderIn(orders: List<Order>): List<OrderProduct>

}