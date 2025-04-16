package kr.hhplus.be.server.domain.product

class ProductInfo {
    data class Find(
        val productId: Long,
        val name: String,
        val price: Int,
        val description: String,
        val quantity: Int
    )

    data class FindAll(
        val products: List<Product>,
        val stocks: List<Stock>,
    )

    companion object {
        fun of(product: Product, stock: Stock): Find {
            return Find(
                productId = product.id,
                name = product.name,
                price = product.price,
                description = product.description,
                quantity = stock.quantity
            )
        }

        fun of(products: List<Product>, stocks: List<Stock>): FindAll {
            return FindAll(
                products = products,
                stocks = stocks
            )
        }
    }

}