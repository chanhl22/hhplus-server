package kr.hhplus.be.server.point

import jakarta.validation.Valid
import kr.hhplus.be.server.point.request.ChargePointRequest
import kr.hhplus.be.server.point.response.ChargePointResponse
import kr.hhplus.be.server.point.response.UserPointResponse
import kr.hhplus.be.server.swagger.PointApi
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PointController : PointApi {

    @GetMapping("/point/{id}")
    override fun find(
        @PathVariable id: Long
    ): UserPointResponse {
        return UserPointResponse(1L, 10000)
    }

    @PatchMapping("/point/{id}/charge")
    override fun charge(
        @PathVariable id: Long,
        @RequestBody @Valid request: ChargePointRequest,
    ): ChargePointResponse {
        return ChargePointResponse(1L, 10000L, 20000L)
    }

}