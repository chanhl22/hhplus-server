package kr.hhplus.be.server.common.annotation

import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext


class CustomSpringELParser {

    companion object {
        fun extractKeys(parameterNames: List<String>, args: Array<Any>, key: String): List<String> {
            val context = StandardEvaluationContext().apply {
                parameterNames.zip(args).forEach { (name, value) ->
                    setVariable(name, value)
                }
            }
            val parser = SpelExpressionParser()
            val expression = parser.parseExpression(key)

            return expression.getValue(context) as? List<String>
                ?: throw IllegalArgumentException("SpEL expression must return List<String>")
        }
    }

}