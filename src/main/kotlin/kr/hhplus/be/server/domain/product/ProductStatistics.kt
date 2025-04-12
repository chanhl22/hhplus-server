package kr.hhplus.be.server.domain.product

import java.time.LocalDateTime

class ProductStatistics(
    val id: Long? = null,
    val product: Product,
    val totalSales: Int,
    val createdAt: LocalDateTime
) {

}