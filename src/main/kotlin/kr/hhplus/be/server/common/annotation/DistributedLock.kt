package kr.hhplus.be.server.common.annotation

import java.util.concurrent.TimeUnit

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DistributedLock(

    val key: String,

    val timeUnit: TimeUnit = TimeUnit.SECONDS,

    val waitTime: Long = 30L,

    val leaseTime: Long = 3L

)
