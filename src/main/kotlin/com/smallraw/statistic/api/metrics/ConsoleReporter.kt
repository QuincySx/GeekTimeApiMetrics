package com.smallraw.statistic.api.metrics

import com.google.gson.Gson
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


class ConsoleReporter(private val metricsStorage: MetricsStorage) {
    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    // 第4个代码逻辑：定时触发第1、2、3代码逻辑的执行；
    fun startRepeatedReport(periodInSeconds: Long, durationInSeconds: Long) {
        executor.scheduleAtFixedRate({ // 第1个代码逻辑：根据给定的时间区间，从数据库中拉取数据；
            val durationInMillis = durationInSeconds * 1000
            val endTimeInMillis = System.currentTimeMillis()
            val startTimeInMillis = endTimeInMillis - durationInMillis
            val requestInfos = metricsStorage.getRequestInfos(startTimeInMillis, endTimeInMillis)
            val stats: MutableMap<String, RequestStat> = HashMap()
            for ((apiName, requestInfosPerApi) in requestInfos) {
                // 第2个代码逻辑：根据原始数据，计算得到统计数据；
                val requestStat = Aggregator.aggregate(requestInfosPerApi, durationInMillis)
                stats[apiName] = requestStat
            }
            // 第3个代码逻辑：将统计数据显示到终端（命令行或邮件）；
            println("Time Span: [$startTimeInMillis, $endTimeInMillis]")
            val gson = Gson()
            println(gson.toJson(stats))
        }, 0, periodInSeconds, TimeUnit.SECONDS)
    }

}

class EmailReporter @JvmOverloads constructor(private val metricsStorage: MetricsStorage, private val emailSender: EmailSender = EmailSender()) {
    private val toAddresses: MutableList<String> = ArrayList()
    fun addToAddress(address: String) {
        toAddresses.add(address)
    }

    fun startDailyReport() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        val firstTime: Date = calendar.time
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                val durationInMillis = DAY_HOURS_IN_SECONDS * 1000
                val endTimeInMillis = System.currentTimeMillis()
                val startTimeInMillis = endTimeInMillis - durationInMillis
                val requestInfos = metricsStorage.getRequestInfos(startTimeInMillis, endTimeInMillis)
                val stats: MutableMap<String, RequestStat> = HashMap()
                for ((apiName, requestInfosPerApi) in requestInfos) {
                    val requestStat = Aggregator.aggregate(requestInfosPerApi, durationInMillis)
                    stats[apiName] = requestStat
                }
                // TODO: 格式化为html格式，并且发送邮件
            }
        }, firstTime, DAY_HOURS_IN_SECONDS * 1000)
    }

    companion object {
        private const val DAY_HOURS_IN_SECONDS = 86400L
    }

}

class EmailSender {

}