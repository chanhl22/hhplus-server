package kr.hhplus.be.server.interfaces.schedule

import kr.hhplus.be.server.application.product.ProductFacade
import kr.hhplus.be.server.domain.product.ProductService
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@EnableScheduling
@Component
class ProductStatisticsScheduler(
    private val productFacade: ProductFacade,
    private val productService: ProductService
) {

    @Scheduled(cron = "0 0 4 * * *")
    fun runDailyAt4AMForStatistics() {
        productFacade.scheduledProductStatistics()
    }

    @Scheduled(cron = "0 5 4 * * *")
    fun scheduledCacheClear() {
        productService.clearProductStatisticsCache()
    }

}