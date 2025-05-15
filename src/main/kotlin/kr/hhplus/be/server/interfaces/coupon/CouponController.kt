package kr.hhplus.be.server.interfaces.coupon

import jakarta.validation.Valid
import kr.hhplus.be.server.domain.coupon.CouponService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CouponController(
    private val couponService: CouponService
) {

    @PostMapping("/coupon/reserve")
    fun reserveFirstCome(
        @RequestBody @Valid request: CouponRequest.FirstCome
    ): ResponseEntity<String> {
        couponService.reserveFirstCome(request.couponId, request.userId)
        return ResponseEntity.ok("쿠폰 요청이 진행중입니다.")
    }

    @GetMapping("/coupon/status")
    fun polling(
        @RequestParam couponId: Long,
        @RequestParam userId: Long,
    ): ResponseEntity<String> {
        val domain = couponService.polling(couponId, userId)
        return ResponseEntity.ok(domain)
    }

}