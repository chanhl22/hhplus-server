package kr.hhplus.be.server.interfaces.coupon

import jakarta.validation.Valid
import kr.hhplus.be.server.application.coupon.CouponFacade
import kr.hhplus.be.server.interfaces.swagger.CouponApi
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CouponController(
    private val couponFacade: CouponFacade
) : CouponApi {

    @PostMapping("/coupon/issue")
    override fun issueCouponFirstCome(
        @RequestBody @Valid request: CouponRequest.FirstComeIssue
    ): CouponResponse.FirstComeIssue {
        val result = couponFacade.issueCouponFirstCome(request.toCriterion())
        return CouponResponse.of(result)
    }
}