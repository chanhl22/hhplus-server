package kr.hhplus.be.server.infrastructure.coupon

import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.coupon.CouponRepository
import org.springframework.stereotype.Repository

@Repository
class CouponRepositoryImpl(
    private val couponJpaRepository: CouponJpaRepository
) : CouponRepository {

    override fun find(couponId: Long): Coupon {
        return couponJpaRepository.findById(couponId)
            .orElseThrow(::IllegalArgumentException)
    }

    override fun findWithPessimisticLock(couponId: Long): Coupon {
        return couponJpaRepository.findWithPessimisticLock(couponId)
            .orElseThrow(::IllegalArgumentException)
    }

    override fun save(coupon: Coupon): Coupon {
        return couponJpaRepository.save(coupon)
    }

}