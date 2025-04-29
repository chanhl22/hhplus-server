package kr.hhplus.be.server.domain.order

class OrderInfo {
    data class ProductStatistics(
        val productId: Long,
        val totalSales: Int
    )

    companion object {
        fun of(productId: Long, totalSales: Int): ProductStatistics {
            return ProductStatistics(
                productId = productId,
                totalSales = totalSales
            )
        }
    }

}