package kr.hhplus.be.server.common.annotation

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RedissonClient
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
class DistributedLockAop(
    private val redissonClient: RedissonClient
) {

    @Around("@annotation(kr.hhplus.be.server.common.annotation.DistributedLock)")
    fun withDistributedLocks(joinPoint: ProceedingJoinPoint): Any {
        val method = (joinPoint.signature as MethodSignature).method
        val distributedLock = method.getAnnotation(DistributedLock::class.java)
        val parameterNames = method.parameters.map { it.name }
        val args = joinPoint.args
        val keys = CustomSpringELParser.extractKeys(parameterNames, args, distributedLock.key)

        val locks = keys.sorted().map { redissonClient.getLock(it) }

        return try {
            for (lock in locks) {
                val success =
                    lock.tryLock(distributedLock.waitTime, distributedLock.leaseTime, distributedLock.timeUnit)
                if (!success) {
                    locks.takeWhile { it != lock }.forEach {
                        if (it.isHeldByCurrentThread) it.unlock()
                    }
                    throw IllegalStateException("락 획득 실패: ${lock.name}")
                }
            }

            joinPoint.proceed()
        } finally {
            for (lock in locks.reversed()) {
                if (lock.isHeldByCurrentThread) {
                    lock.unlock()
                }
            }
        }
    }

}