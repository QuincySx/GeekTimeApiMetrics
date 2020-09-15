package com.smallraw.statistic.api.config

import com.smallraw.statistic.api.metrics.*
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import javax.annotation.Resource


@Configuration
class MetricsConfig {
//    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
//    @Bean
//    fun getMetricsStorage(): MetricsStorage {
//        return MemoryMetricsStorage()
//    }

    @Resource(name = "metricsStorage")
    private lateinit var mMetricsStorage: MetricsStorage

    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Bean
    fun getConsoleReporter(): ConsoleReporter {
        val consoleReporter = ConsoleReporter(mMetricsStorage)
        consoleReporter.startRepeatedReport(60, 60)
        return consoleReporter
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Bean
    fun getEmailReporter(): EmailReporter {
        val emailReporter = EmailReporter(mMetricsStorage)
        emailReporter.addToAddress("wangzheng@xzg.com")
        emailReporter.startDailyReport()
        return emailReporter
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Bean
    fun getMetricsCollector(): MetricsCollector {
        return MetricsCollector(mMetricsStorage)
    }
}