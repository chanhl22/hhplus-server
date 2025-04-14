package kr.hhplus.be.server.domain.product

class ProductCommands {
    data class ProductsCommand (
        val products: List<ProductCommand>
    ) {
        companion object {
            fun of(products: List<ProductCommand>): ProductsCommand {
                return ProductsCommand(
                    products = products
                )
            }
        }

        fun getProductIds() : List<Long> {
            return products.map { product ->
                product.productId
            }
        }
    }

    data class ProductCommand(
        val productId: Long,
        val quantity: Int
    )
}
