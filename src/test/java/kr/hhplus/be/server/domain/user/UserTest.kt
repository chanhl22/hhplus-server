//package kr.hhplus.be.server.domain.user
//
//import kr.hhplus.be.server.fixture.user.UserFixture
//import org.assertj.core.api.Assertions.assertThatCode
//import org.assertj.core.api.Assertions.assertThatThrownBy
//import org.junit.jupiter.api.DisplayName
//import org.junit.jupiter.api.Test
//
//class UserTest {
//
//    @DisplayName("충전된 금액이 없으면 예외가 발생한다.")
//    @Test
//    fun validatePointUsable() {
//        //given
//        val user = UserFixture.create(balance = 0)
//
//        //when //then
//        assertThatThrownBy { user.validatePointUsable() }
//            .isInstanceOf(IllegalArgumentException::class.java)
//            .hasMessage("충전된 금액이 없습니다.")
//    }
//
//    @DisplayName("충전된 금액이 있으면 예외가 발생하지 않는다.")
//    @Test
//    fun validatePointUsable2() {
//        //given
//        val user = UserFixture.create(balance = 1)
//
//        // When & Then
//        assertThatCode { user.validatePointUsable() }
//            .doesNotThrowAnyException()
//    }
//
//}