package kr.hhplus.be.server.domain.product

@Suppress("unused")
class ProductDomains {
    data class ProductSalesInfo(
        val productId: Long,
        val name: String,
        val price: Int,
        val soldQuantity: Int,
        val rank: Int
    ) {
        companion object {
            fun of(product: Product, totalSales: Int, rank: Int): ProductSalesInfo {
                return ProductSalesInfo(
                    productId = product.id,
                    name = product.name,
                    price = product.price,
                    soldQuantity = totalSales,
                    rank = rank
                )
            }
        }
    }
}
