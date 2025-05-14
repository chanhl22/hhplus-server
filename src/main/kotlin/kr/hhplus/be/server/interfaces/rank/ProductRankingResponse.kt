package kr.hhplus.be.server.interfaces.rank

import kr.hhplus.be.server.domain.rank.ProductRanking

class ProductRankingResponse {
    data class Daily(
        val productId: Long,
        val productName: String,
        val rank: Int
    )

    data class Weekly(
        val productId: Long,
        val productName: String,
        val rank: Int
    )

    companion object {
        fun fromDaily(domain: ProductRanking): Daily {
            return Daily(
                productId = domain.productId,
                productName = domain.productName,
                rank = domain.rank
            )
        }

        fun fromWeekly(domain: ProductRanking): Weekly {
            return Weekly(
                productId = domain.productId,
                productName = domain.productName,
                rank = domain.rank
            )
        }
    }

}