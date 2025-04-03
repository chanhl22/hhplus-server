package kr.hhplus.be.server.coupon

import jakarta.validation.Valid
import kr.hhplus.be.server.coupon.request.CouponRequest
import kr.hhplus.be.server.coupon.response.CouponResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CouponController {

    @PostMapping("/coupon/{id}/issue")
    fun point(
        @PathVariable id: Long,
        @RequestBody @Valid request: CouponRequest
    ): CouponResponse {
        return CouponResponse(1L, 1L, 1000)
    }

    @GetMapping("/coupons")
    fun coupons(
        @RequestBody @Valid request: CouponRequest
    ): CouponResponse {
        return CouponResponse(1L, 1L, 1000)
    }

}