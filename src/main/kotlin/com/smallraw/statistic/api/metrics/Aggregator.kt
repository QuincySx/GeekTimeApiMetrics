package com.smallraw.statistic.api.metrics

import java.util.*

data class RequestStat(
        var maxResponseTime: Double = 0.0,
        var minResponseTime: Double = 0.0,
        var avgResponseTime: Double = 0.0,
        var p999ResponseTime: Double = 0.0,
        var p99ResponseTime: Double = 0.0,
        var count: Long = 0,
        var tps: Long = 0
)

class Aggregator {
    fun aggregate(
            requestInfos: Map<String, List<RequestInfo>>, durationInMillis: Long): Map<String, RequestStat> {
        val requestStats: MutableMap<String, RequestStat> = HashMap()
        for ((apiName, requestInfosPerApi) in requestInfos) {
            val requestStat = doAggregate(requestInfosPerApi, durationInMillis)
            requestStats[apiName] = requestStat
        }
        return requestStats
    }

    private fun doAggregate(requestInfos: List<RequestInfo>, durationInMillis: Long): RequestStat {
        val respTimes: MutableList<Double> = ArrayList()
        for ((_, respTime) in requestInfos) {
            respTimes.add(respTime)
        }
        val requestStat = RequestStat()
        requestStat.maxResponseTime = max(respTimes)
        requestStat.minResponseTime = min(respTimes)
        requestStat.avgResponseTime = avg(respTimes)
        requestStat.p999ResponseTime = percentile999(respTimes)
        requestStat.p99ResponseTime = percentile99(respTimes)
        requestStat.count = respTimes.size.toLong()
        requestStat.tps = tps(respTimes.size, durationInMillis / 1000.toDouble()).toLong()
        return requestStat
    }

    // 以下的函数的代码实现均省略...
    private fun max(dataset: List<Double>): Double {
        return dataset.maxOrNull() ?: 0.0
    }

    private fun min(dataset: List<Double>): Double {
        return dataset.minOrNull() ?: 0.0
    }

    private fun avg(dataset: List<Double>): Double {
        return dataset.average()
    }

    private fun tps(count: Int, duration: Double): Double {
        return count / duration
    }

    private fun percentile999(dataset: List<Double>): Double {
        val idx999 = dataset.size * 0.999
        return percentile(dataset, idx999)
    }

    private fun percentile99(dataset: List<Double>): Double {
        val idx99 = dataset.size * 0.99
        return percentile(dataset, idx99)
    }

    private fun percentile(dataset: List<Double>, ratio: Double): Double {
        return dataset[ratio.toInt()]
    }
}