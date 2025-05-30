package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.order.OrderedProducts
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
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
    private val productStatisticsRepository: ProductStatisticsRepository,
    private val productEventPublisher: ProductEventPublisher
) {
    fun find(productId: Long): ProductInfo.Find {
        val product = productRepository.find(productId)
        val stock = stockRepository.findByProductId(product.id)

        return ProductInfo.of(product, stock)
    }

    @Transactional
    fun deduct(command: ProductCommand.Deduct) {
        val products = productRepository.findAllByIdIn(command.getProductIds())
        val stocks = stockRepository.findByProductIdIn(products.map { it.id })

        val orderedProducts = OrderedProducts.create(
            products,
            stocks,
            command.createOrderProductQuantityCountMap()
        )
        orderedProducts.isEmptyOrder()
        orderedProducts.isEnoughQuantity()
        val totalPrice = orderedProducts.calculateTotalPrice()

        val orderProductQuantityMap = command.createOrderProductQuantityCountMap()
        val deductStocks = stocks.map { stock ->
            val orderQuantity = orderProductQuantityMap[stock.productId]
                ?: throw IllegalArgumentException("존재하지 않는 상품입니다. productId=${stock.productId}")
            stock.deduct(orderQuantity)
        }
        stockRepository.saveAll(deductStocks)

        productEventPublisher.publish(command.toEvent(totalPrice, orderedProducts))
    }

    fun saveProductStatistics(commands: List<ProductCommand.ProductStatistics>) {
        productStatisticsRepository.saveAll(
            commands.map { command ->
                ProductStatistics.create(command.productId, command.totalSales)
            })
    }

    @Cacheable(cacheNames = ["findTopSellingProducts"])
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

    @CacheEvict(cacheNames = ["findTopSellingProducts"], allEntries = true)
    fun clearProductStatisticsCache() {
    }

}

