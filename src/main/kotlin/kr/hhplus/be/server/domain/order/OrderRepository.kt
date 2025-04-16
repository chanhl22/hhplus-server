package kr.hhplus.be.server.domain.order

interface OrderRepository {

    fun save(order: Order): Order

}