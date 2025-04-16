package kr.hhplus.be.server.domain.product

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import java.time.LocalDateTime

@Entity
class ProductStatistics(
    @OneToOne(fetch = FetchType.LAZY)
    val product: Product,
    val totalSales: Int,
    val createdAt: LocalDateTime,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) {

}