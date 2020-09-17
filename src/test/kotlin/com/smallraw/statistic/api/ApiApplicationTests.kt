package com.smallraw.statistic.api

import com.smallraw.statistic.api.metrics.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class ApiApplicationTests {

    @Test
    fun contextLoads() {
    }

    @Test
    fun testMetrics() {
        val storage: MetricsStorage = RedisMetricsStorage()
        val aggregator = Aggregator()

        val consoleViewer = ConsoleViewer()

        val consoleReporter = ConsoleReporter(storage, aggregator, consoleViewer)

        consoleReporter.startRepeatedReport(60, 60)

        val collector = MetricsCollector(storage)
        collector.recordRequest(RequestInfo("register", 123.0, 10234))
        collector.recordRequest(RequestInfo("register", 223.0, 11234))
        collector.recordRequest(RequestInfo("register", 323.0, 12334))
        collector.recordRequest(RequestInfo("login", 23.0, 12434))
        collector.recordRequest(RequestInfo("login", 1223.0, 14234))
        try {
            Thread.sleep(100000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}
