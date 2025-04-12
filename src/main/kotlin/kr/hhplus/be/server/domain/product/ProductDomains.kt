package kr.hhplus.be.server.domain.product

@Suppress("unused")
class ProductDomains {
    data class ProductSalesInfo(
        val product: Product,
        val totalSales: Int,
        val rank: Int
    ) {
        companion object {
            fun of(product: Product, totalSales: Int, rank: Int): ProductSalesInfo {
                return ProductSalesInfo(
                    product = product,
                    totalSales = totalSales,
                    rank = rank
                )
            }
        }
    }
}
