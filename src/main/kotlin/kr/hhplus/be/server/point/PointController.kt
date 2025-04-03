package kr.hhplus.be.server.point

import jakarta.validation.Valid
import kr.hhplus.be.server.point.request.UserPointRequest
import kr.hhplus.be.server.point.response.UserPointResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PointController {

    @GetMapping("/point/{id}")
    fun point(
        @PathVariable id: Long,
    ): UserPointResponse {
        return UserPointResponse(1L, 10000)
    }

    @PatchMapping("/point/{id}/charge")
    fun charge(
        @PathVariable id: Long,
        @RequestBody @Valid request: UserPointRequest,
    ): UserPointResponse {
        return UserPointResponse(1L, 10000)
    }

}