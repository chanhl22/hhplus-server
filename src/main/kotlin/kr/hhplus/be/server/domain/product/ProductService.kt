package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.product.ProductDomains.ProductSalesInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
@Transactional(readOnly = true)
class ProductService(
    private val productRepository: ProductRepository,
    private val stockRepository: StockRepository,
    private val productStatisticsRepository: ProductStatisticsRepository
) {
    fun find(productId: Long): ProductInfo.Find {
        val product = productRepository.find(productId)
        val stock = stockRepository.findByProductId(product.id)

        return ProductInfo.of(product, stock)
    }

    fun findAll(productIds: List<Long>): ProductInfo.FindAll {
        val products = productRepository.findAllByIdIn(productIds)
        val stocks = stockRepository.findByProductIdIn(products.map { it.id })

        return ProductInfo.of(products, stocks)
    }

    fun findTopSellingProducts(): List<ProductSalesInfo> {
        val startDatetime = startThreeDaysAgoDate()
        val endDatetime = endCurrentDate()
        val productsStatistics =
            productStatisticsRepository.findAllByCreatedAtBetween(startDatetime, endDatetime)

        return extractTop5Product(productsStatistics)
    }

    private fun startThreeDaysAgoDate(): LocalDateTime {
        return LocalDateTime.of(LocalDate.now().minusDays(3), LocalTime.of(0, 0, 0))
    }

    private fun endCurrentDate(): LocalDateTime {
        return LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59))
    }

    private fun extractTop5Product(productsStatistics: List<ProductStatistics>): List<ProductSalesInfo> {
        return productsStatistics
            .groupBy { it.product }
            .map { (product, stats) ->
                product to stats.sumOf { it.totalSales }
            }
            .sortedByDescending { (_, totalSales) -> totalSales }
            .take(5)
            .mapIndexed { index, (product, totalSales) ->
                ProductSalesInfo.of(
                    product = product,
                    totalSales = totalSales,
                    rank = index + 1
                )
            }
    }

}

