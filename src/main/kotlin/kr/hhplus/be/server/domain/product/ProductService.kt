package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.product.ProductCommands.ProductsCommand
import kr.hhplus.be.server.domain.product.ProductDomains.ProductSalesInfo
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val productStatisticsRepository: ProductStatisticsRepository
) {
    fun find(productId: Long): Product {
        return productRepository.find(productId)
    }

    fun findAll(command: ProductsCommand): List<Product> {
        val products = productRepository.findAllWithStockByIdIn(command.getProductIds())

        val productMap: Map<Long, Product> = products.associateBy { it.id }
        return command.products.map { commandProduct ->
            val product = productMap[commandProduct.productId]
                ?: throw IllegalArgumentException("존재하지 않는 상품입니다. id: ${commandProduct.productId}")
            product.validateStockEnough(commandProduct.quantity)
            product
        }
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

