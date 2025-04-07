package kr.hhplus.be.server.coupon

import jakarta.validation.Valid
import kr.hhplus.be.server.coupon.request.FirstComeCouponRequest
import kr.hhplus.be.server.coupon.response.CouponsResponse
import kr.hhplus.be.server.coupon.response.FirstComeCouponResponse
import kr.hhplus.be.server.swagger.CouponApi
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class CouponController : CouponApi {

    @PostMapping("/coupon/issue")
    override fun issueCouponFirstCome(
        @RequestBody @Valid request: FirstComeCouponRequest
    ): FirstComeCouponResponse {
        return FirstComeCouponResponse(
            true,
            1L,
            "10% 할인 쿠폰",
            "PERCENTAGE",
            10,
            LocalDateTime.of(2024, 4, 10, 23, 59),
            LocalDateTime.of(2024, 4, 3, 12, 0)
        )
    }

    @GetMapping("/coupons/{id}")
    override fun findCoupons(
        @PathVariable id: Long
    ): List<CouponsResponse> {
        return listOf(
            CouponsResponse(
                1L,
                "10% 할인 쿠폰",
                "PERCENTAGE",
                10,
                LocalDateTime.of(2024, 4, 10, 23, 59),
                LocalDateTime.of(2024, 4, 3, 12, 0),
                false
            ),

            CouponsResponse(
                2L,
                "5,000원 할인 쿠폰",
                "FIXED_AMOUNT",
                5000,
                LocalDateTime.of(2024, 4, 10, 23, 59),
                LocalDateTime.of(2024, 4, 3, 12, 0),
                true
            ),
        )
    }

}