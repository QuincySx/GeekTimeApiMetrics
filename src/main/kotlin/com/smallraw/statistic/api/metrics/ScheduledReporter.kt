package com.smallraw.statistic.api.metrics

import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


abstract class ScheduledReporter(protected var metricsStorage: MetricsStorage, protected var aggregator: Aggregator, protected var viewer: StatViewer) {
    protected fun doStatAndReport(startTimeInMillis: Long, endTimeInMillis: Long) {
        val durationInMillis = endTimeInMillis - startTimeInMillis
        val requestInfos = metricsStorage.getRequestInfos(startTimeInMillis, endTimeInMillis)
        val requestStats = aggregator.aggregate(requestInfos, durationInMillis)
        viewer.output(requestStats, startTimeInMillis, endTimeInMillis)
    }
}

class ConsoleReporter(metricsStorage: MetricsStorage, aggregator: Aggregator, viewer: StatViewer)
    : ScheduledReporter(metricsStorage, aggregator, viewer) {
    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    fun startRepeatedReport(periodInSeconds: Long, durationInSeconds: Long) {
        executor.scheduleAtFixedRate({
            val durationInMillis = durationInSeconds * 1000
            val endTimeInMillis = System.currentTimeMillis()

            doStatAndReport(durationInMillis, endTimeInMillis)
        }, 0L, periodInSeconds, TimeUnit.SECONDS)
    }
}

class EmailReporter(metricsStorage: MetricsStorage, aggregator: Aggregator, viewer: StatViewer)
    : ScheduledReporter(metricsStorage, aggregator, viewer) {
    fun startDailyReport() {
        val firstTime = trimTimeFieldsToZeroOfNextDay()
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                val durationInMillis = DAY_HOURS_IN_SECONDS * 1000
                val endTimeInMillis = System.currentTimeMillis()
                doStatAndReport(durationInMillis, endTimeInMillis)
            }
        }, firstTime, DAY_HOURS_IN_SECONDS * 1000)
    }

    protected fun trimTimeFieldsToZeroOfNextDay(date: Date = Date()): Date {
        val calendar = Calendar.getInstance() // 这里可以获取当前时间
        calendar.time = date
        calendar.add(Calendar.DATE, 1)
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }

    companion object {
        private const val DAY_HOURS_IN_SECONDS = 86400L
    }
}