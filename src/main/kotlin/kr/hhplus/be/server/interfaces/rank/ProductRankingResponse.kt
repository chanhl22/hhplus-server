package kr.hhplus.be.server.interfaces.rank

import kr.hhplus.be.server.domain.rank.ProductRanking

class ProductRankingResponse {
    data class Daily(
        val productId: Long,
        val productName: String,
        val rank: Int
    )

    companion object {
        fun from(domain: ProductRanking): Daily {
            return Daily(
                productId = domain.productId,
                productName = "",
                rank = domain.rank
            )
        }
    }

}