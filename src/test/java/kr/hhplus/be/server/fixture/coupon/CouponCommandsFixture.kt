package kr.hhplus.be.server.fixture.coupon

import kr.hhplus.be.server.domain.coupon.CouponCommands.IssueCouponCommand
import kr.hhplus.be.server.domain.user.User
import kr.hhplus.be.server.fixture.user.UserDomainFixture

object IssueCouponCommandFixture {
    fun create(
        user: User = UserDomainFixture.create(),
        couponId: Long = 1L
    ): IssueCouponCommand {
        return IssueCouponCommand(
            user = user,
            couponId = couponId
        )
    }

}