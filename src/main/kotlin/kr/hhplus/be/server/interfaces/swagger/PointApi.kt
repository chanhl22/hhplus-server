package kr.hhplus.be.server.interfaces.swagger

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import kr.hhplus.be.server.interfaces.point.PointRequest
import kr.hhplus.be.server.interfaces.point.PointResponse

@Tag(name = "Point API", description = "포인트 API")
interface PointApi {

    @Operation(summary = "잔액 조회", description = "사용자의 잔액을 조회합니다.")
    @Parameter(name = "id", description = "사용자 ID", example = "1")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "성공적으로 데이터를 반환합니다.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            example = """{
                                            "userId": 1,
                                            "balance": 10000
                                         }"""
                        )
                    )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "해당 정보가 존재하지 않습니다.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            example = """{
                                            "code": "400",
                                            "message": "잘못된 요청입니다."
                                         }"""
                        )
                    )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "알 수 없는 에러가 발생했습니다.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            example = """{
                                            "code": "500",
                                            "message": "알 수 없는 에러가 발생했습니다."
                                         }"""
                        )
                    )]
            )
        ]
    )
    fun find(userId: Long): PointResponse.Find

    @Operation(summary = "금액 충전", description = "충전할 금액을 받아 잔액을 충전합니다.")
    @Parameter(name = "id", description = "사용자 ID", example = "1")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "성공적으로 데이터를 반환합니다.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            example = """{
                                            "userId": 1,
                                            "chargedAmount": 10000,
                                            "balance": 20000
                                         }"""
                        )
                    )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "해당 정보가 존재하지 않습니다.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            example = """{
                                            "code": "400",
                                            "message": "잘못된 요청입니다."
                                         }"""
                        )
                    )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "알 수 없는 에러가 발생했습니다.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            example = """{
                                            "code": "500",
                                            "message": "알 수 없는 에러가 발생했습니다."
                                         }"""
                        )
                    )]
            )
        ]
    )
    fun charge(
        userId: Long,
        @RequestBody(
            description = "충전할 포인트 정보",
            required = true,
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(
                        implementation = PointRequest.Charge::class,
                        example = """{
                            "amount": 10000
                        }"""
                    )
                )
            ]
        )
        request: PointRequest.Charge
    ): PointResponse.Charge

}