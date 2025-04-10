package kr.hhplus.be.server.interfaces.swagger

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import kr.hhplus.be.server.interfaces.product.ProductResponse
import kr.hhplus.be.server.interfaces.product.response.TopSellingProductResponse

@Tag(name = "Product API", description = "상품 API")
interface ProductApi {

    @Operation(summary = "상품 조회", description = "상품 정보를 조회합니다.")
    @Parameter(name = "id", description = "상품 ID", example = "1")
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
                                            "productId": 1,
                                            "name": "무선 블루투스 이어폰",
                                            "price": 129000,
                                            "stock": 25,
                                            "description": "고음질 무선 블루투스 이어폰. 최대 20시간 사용 가능."
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
    fun find(id: Long): ProductResponse.FoundProduct

    @Operation(summary = "상위 상품 조회", description = "최근 3일간 가장 많이 팔린 상위 5개 상품 정보를 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "성공적으로 데이터를 반환합니다.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            example = """[
                                            {
                                              "rank": 1,
                                              "productId": 10,
                                              "name": "무선 블루투스 이어폰",
                                              "price": 129000,
                                              "soldQuantity": 350,
                                              "stock": 25
                                            },
                                            {
                                              "rank": 2,
                                              "productId": 5,
                                              "name": "게이밍 키보드",
                                              "price": 89000,
                                              "soldQuantity": 280,
                                              "stock": 50
                                            }
                                         ]"""
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
    fun findTopSellingProducts(): List<TopSellingProductResponse>

}