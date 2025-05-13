package kr.hhplus.be.server.domain.rank

class ProductRanking(
    val productId: Long,
//    val productName: String,
    val rank: Int
) {
    companion object {
//        fun fromListWithNames(
//            idToScoreList: List<Pair<Long, Double?>>,
//            nameMap: Map<Long, String>
//        ): List<ProductRanking> {
//            return idToScoreList.mapIndexed { index, (productId, _) ->
//                ProductRanking(
//                    productId = productId,
//                    productName = nameMap[productId] ?: "이름 없음",
//                    rank = index + 1
//                )
//            }
//        }

        fun of(productId: String, rank: Int): ProductRanking {
            return ProductRanking(
                productId = productId.toLong(),
                rank = rank + 1
            )
        }
    }

}