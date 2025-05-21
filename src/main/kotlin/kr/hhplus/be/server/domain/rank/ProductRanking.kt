package kr.hhplus.be.server.domain.rank

class ProductRanking(
    val productId: Long,
    val productName: String,
    val rank: Int
) {
    companion object {
        fun of(productId: String, name: String?, rank: Int): ProductRanking {
            return ProductRanking(
                productId = productId.toLong(),
                productName = name ?: "",
                rank = rank + 1
            )
        }
    }

}