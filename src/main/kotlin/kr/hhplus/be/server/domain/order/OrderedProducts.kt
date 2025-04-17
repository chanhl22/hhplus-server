package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.product.Stock

class OrderedProducts(
    val products: List<OrderedProduct>,
    val orderProductQuantityCountMap: Map<Long, Int>
) {
    companion object {
        fun create(
            products: List<Product>,
            stocks: List<Stock>,
            orderProductQuantityCountMap: Map<Long, Int>
        ): OrderedProducts {
            validateStocks(stocks)

            val stockMap: Map<Long, Stock> = stocks.associateBy { it.productId }
            val orderedProducts = createOrderedProducts(products, stockMap)

            return OrderedProducts(
                products = orderedProducts,
                orderProductQuantityCountMap = orderProductQuantityCountMap
            )
        }

        private fun validateStocks(stocks: List<Stock>) {
            stocks.forEach { it.validateQuantity() }
        }

        private fun createOrderedProducts(
            products: List<Product>,
            stockMap: Map<Long, Stock>
        ): List<OrderedProduct> {
            val orderedProducts = products.map { product ->
                val stock = stockMap[product.id]
                    ?: throw IllegalArgumentException("재고 정보가 없습니다. productId=${product.id}")

                OrderedProduct.create(product.id, product.price, stock.quantity)
            }
            return orderedProducts
        }
    }

    fun isEmptyOrder() {
        if (orderProductQuantityCountMap.isEmpty()) {
            throw IllegalArgumentException("주문 항목이 비어있습니다.")
        }
    }

    fun calculateTotalPrice(): Int {
        return products.sumOf { product ->
            val orderQuantity = orderProductQuantityCountMap[product.productId]
                ?: throw IllegalArgumentException("존재하지 않는 상품입니다. productId=${product.productId}")
            orderQuantity * product.price
        }
    }

    fun isEnoughQuantity() {
        products.map { product ->
            val quantity = product.quantity
            val requestQuantity = (orderProductQuantityCountMap[product.productId]
                ?: throw IllegalArgumentException("재고 정보가 없습니다. productId=${product.productId}"))

            if (quantity < requestQuantity) {
                throw IllegalArgumentException("주문한 상품 재고가 부족합니다.")
            }
        }
    }

    fun getProductIds(): List<Long> {
        return products.map { it.productId }
    }

}

class OrderedProduct(
    val productId: Long,
    val price: Int,
    val quantity: Int
) {
    companion object {
        fun create(productId: Long, price: Int, quantity: Int): OrderedProduct {
            return OrderedProduct(
                productId = productId,
                price = price,
                quantity = quantity
            )
        }
    }

}
