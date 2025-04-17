package kr.hhplus.be.server.infrastructure.product

import kr.hhplus.be.server.domain.product.ProductStatistics
import kr.hhplus.be.server.domain.product.ProductWithStatDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface ProductStatisticsJpaRepository : JpaRepository<ProductStatistics, Long> {

    fun findAllByCreatedAtBetween(start: LocalDateTime, end: LocalDateTime): List<ProductStatistics>


//    """
//    SELECT
//        ps.product_id AS productId,
//        p.name AS productName,
//        p.price,
//        p.description,
//        ps.total_sales AS totalSales,
//        ps.created_at AS createdAt
//    FROM product_statistics ps
//    JOIN product p ON ps.product_id = p.id
//    WHERE ps.created_at BETWEEN :start AND :end
//    """


//    """
//    SELECT
//    ps.product_id AS productId,
//    p.name AS productName,
//    p.price,
//    p.description,
//    SUM(ps.total_sales) AS totalSalesSum
//FROM product_statistics ps
//JOIN product p ON ps.product_id = p.id
//WHERE ps.created_at BETWEEN :start AND :end
//GROUP BY ps.product_id, p.name, p.price, p.description
//ORDER BY totalSalesSum DESC
//LIMIT 5
//    """
    @Query(
        """
    SELECT 
    ps.product_id AS productId,
    p.name AS productName,
    p.price,
    p.description,
    SUM(ps.total_sales) AS totalSalesSum
FROM product_statistics ps
JOIN product p ON ps.product_id = p.id
WHERE ps.created_at BETWEEN :start AND :end
GROUP BY ps.product_id, p.name, p.price, p.description
ORDER BY totalSalesSum DESC
LIMIT 5
    """,
        nativeQuery = true
    )
    fun findStatsWithProductInfo(
        @Param("start") start: LocalDateTime,
        @Param("end") end: LocalDateTime
    ): List<ProductWithStatDto>

}