package com.smallraw.statistic.api.config

import com.smallraw.statistic.api.metrics.ConsoleReporter
import com.smallraw.statistic.api.metrics.MetricsCollector
import com.smallraw.statistic.api.metrics.MetricsStorage
import com.smallraw.statistic.api.metrics.RedisMetricsStorage
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope


@Configuration
class MetricsConfig {
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Bean
    fun getMetricsStorage(): MetricsStorage {
        return RedisMetricsStorage()
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Bean
    fun getConsoleReporter(metricsStorage: MetricsStorage): ConsoleReporter {
        val consoleReporter = ConsoleReporter(metricsStorage)
        consoleReporter.startRepeatedReport(60, 60)
        return consoleReporter
    }

//    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
//    @Bean
//    fun getEmailReporter(metricsStorage: MetricsStorage): EmailReporter {
//        val emailReporter = EmailReporter(metricsStorage)
//        emailReporter.addToAddress("wangzheng@xzg.com")
//        emailReporter.startDailyReport()
//        return emailReporter
//    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Bean
    fun getMetricsCollector(metricsStorage: MetricsStorage): MetricsCollector {
        return MetricsCollector(metricsStorage)
    }
}