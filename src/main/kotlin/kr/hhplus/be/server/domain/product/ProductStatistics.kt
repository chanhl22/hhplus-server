package kr.hhplus.be.server.domain.product

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "product_statistics")
class ProductStatistics(

    val productId: Long,

    val totalSales: Int,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) {
    companion object {
        fun create(productId: Long, totalSales: Int): ProductStatistics {
            return ProductStatistics(
                productId = productId,
                totalSales = totalSales,
                createdAt = LocalDateTime.now()
            )
        }
    }

}