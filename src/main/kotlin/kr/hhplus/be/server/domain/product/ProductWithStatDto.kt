package kr.hhplus.be.server.domain.product

import java.time.LocalDateTime

interface ProductWithStatDto {
//    fun getProductId(): Long
//    fun getProductName(): String
//    fun getPrice(): Int
//    fun getDescription(): String
//    fun getTotalSales(): Int
//    fun getCreatedAt(): LocalDateTime

    fun getProductId(): Long
    fun getProductName(): String
    fun getPrice(): Int
    fun getDescription(): String
    fun getTotalSalesSum(): Int
}