package kr.hhplus.be.server.infrastructure.order

import kr.hhplus.be.server.domain.order.OrderProduct
import org.springframework.data.jpa.repository.JpaRepository

interface OrderProductJpaRepository : JpaRepository<OrderProduct, Long>