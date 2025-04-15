package kr.hhplus.be.server.interfaces.user

import jakarta.validation.Valid
import kr.hhplus.be.server.application.point.PointFacade
import kr.hhplus.be.server.interfaces.swagger.PointApi
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PointController(
    private val pointFacade: PointFacade
) : PointApi {

    @GetMapping("/point")
    override fun find(
        @RequestParam userId: Long
    ): PointResponse.Find {
        val result = pointFacade.find(userId)
        return PointResponse.from(result)
    }

    @PatchMapping("/point/charge")
    override fun charge(
        @RequestParam userId: Long,
        @RequestBody @Valid request: PointRequest.Charge
    ): PointResponse.Charge {
        val criteria = request.toCriteria(userId)
        val result = pointFacade.charge(criteria)
        return PointResponse.from(result)
    }

}