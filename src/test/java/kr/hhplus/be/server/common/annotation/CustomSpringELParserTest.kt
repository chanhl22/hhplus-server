package kr.hhplus.be.server.common.annotation

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CustomSpringELParserTest {

    @DisplayName("SpEL 표현식에서 키를 추출합니다.")
    @Test
    fun extractKeys() {
        // given
        val parameterNames = listOf("ids")
        val args: Array<Any> = arrayOf(
            listOf("k1", "k2", "k3")
        )
        val key = "#ids"

        // when
        val result = CustomSpringELParser.extractKeys(parameterNames, args, key)

        // then
        assertThat(result).containsExactly("k1", "k2", "k3")
    }

    @DisplayName("SpEL 표현식의 결과가 문자열 리스트가 아니면 예외가 발생한다.")
    @Test
    fun cannotExtractKeys() {
        // given
        val parameterNames = listOf("id")
        val args: Array<Any> = arrayOf("k1")
        val key = "#id"

        // when // then
        assertThatThrownBy { CustomSpringELParser.extractKeys(parameterNames, args, key) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("SpEL expression must return List<String>")
    }

}
