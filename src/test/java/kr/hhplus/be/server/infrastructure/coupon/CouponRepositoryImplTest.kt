package kr.hhplus.be.server.infrastructure.coupon

import kr.hhplus.be.server.domain.coupon.DiscountType
import kr.hhplus.be.server.fixture.coupon.CouponDomainFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class CouponRepositoryImplTest {

    @Autowired
    private lateinit var couponRepositoryImpl: CouponRepositoryImpl

    @Autowired
    private lateinit var couponJpaRepository: CouponJpaRepository

    @DisplayName("쿠폰을 조회한다.")
    @Test
    fun find() {
        //given
        val coupon = CouponDomainFixture.create(couponId = 0L)
        couponJpaRepository.save(coupon)

        //when
        val result = couponRepositoryImpl.find(coupon.id)

        //then
        assertThat(result)
            .extracting("name", "discountType", "discountValue")
            .containsExactly(
                "1000원 할인 쿠폰", DiscountType.AMOUNT, 1000
            )
    }

    @DisplayName("쿠폰을 저장한다.")
    @Test
    fun save() {
        //given
        val coupon = CouponDomainFixture.create(couponId = 0L)

        //when
        val result = couponRepositoryImpl.save(coupon)

        //then
        assertThat(result)
            .extracting("name", "discountType", "discountValue")
            .containsExactly(
                "1000원 할인 쿠폰", DiscountType.AMOUNT, 1000
            )
    }

}