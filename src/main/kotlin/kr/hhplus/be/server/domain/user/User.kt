package kr.hhplus.be.server.domain.user

import kr.hhplus.be.server.domain.coupon.UserCoupon

class User(
    val id: Long,
    val name: String,
    val pointId: Long,
    val userCoupons: MutableList<UserCoupon> = mutableListOf()
) {
//    fun validatePointUsable() {
//        if (isEmptyBalance()) {
//            throw IllegalArgumentException("충전된 금액이 없습니다.")
//        }
//    }
//
//    private fun isEmptyBalance() = point.balance <= 0
}
