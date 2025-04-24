package kr.hhplus.be.server.domain.product

import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
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

    @Retryable(
        value = [ObjectOptimisticLockingFailureException::class],
        maxAttempts = 2,
        backoff = Backoff(delay = 10)
    )
    @Transactional
    fun deduct(command: ProductCommand.Deduct) {
        val stocks = stockRepository.findByProductIdInWithOptimisticLock(command.getProductIds())

        val orderProductQuantityMap = command.products.associate { it.productId to it.quantity }
        val deductStocks = stocks.map { stock ->
            val orderQuantity = orderProductQuantityMap[stock.productId]
                ?: throw IllegalArgumentException("존재하지 않는 상품입니다. productId=${stock.productId}")
            stock.deduct(orderQuantity)
        }

        stockRepository.saveAll(deductStocks)
    }

    fun saveProductStatistics(commands: List<ProductCommand.ProductStatistics>) {
        productStatisticsRepository.saveAll(
            commands.map { command ->
                ProductStatistics.create(command.productId, command.totalSales)
            })
    }

    fun findTopSellingProducts(): List<ProductInfo.FindTopSales> {
        val startDatetime = startThreeDaysAgoDate()
        val endDatetime = endCurrentDate()
        val productsStatistics =
            productStatisticsRepository.findAllByCreatedAtBetween(startDatetime, endDatetime)

        val products = productRepository.findAllByIdIn(productsStatistics.map { it.productId })
        val productMap: Map<Long, Product> = products.associateBy { it.id }

        return extractTop5Product(productsStatistics, productMap)
    }

    private fun startThreeDaysAgoDate(): LocalDateTime {
        return LocalDateTime.of(LocalDate.now().minusDays(3), LocalTime.of(0, 0, 0))
    }

    private fun endCurrentDate(): LocalDateTime {
        return LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59))
    }

    private fun extractTop5Product(
        productsStatistics: List<ProductStatistics>,
        productMap: Map<Long, Product>
    ): List<ProductInfo.FindTopSales> {
        return productsStatistics
            .groupBy { it.productId }
            .map { (productId, stats) ->
                productId to stats.sumOf { it.totalSales }
            }
            .sortedByDescending { (_, totalSales) -> totalSales }
            .take(5)
            .mapIndexed { index, (productId, totalSales) ->
                val product = productMap[productId]
                    ?: throw IllegalArgumentException("존재하지 않는 상품입니다. productId=${productId}")
                ProductInfo.of(
                    product = product,
                    totalSales = totalSales,
                    rank = index + 1
                )
            }
    }

}

