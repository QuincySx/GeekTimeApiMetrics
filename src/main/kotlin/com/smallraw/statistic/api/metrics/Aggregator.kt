package com.smallraw.statistic.api.metrics

import java.util.*


class Aggregator {
    companion object {
        fun aggregate(requestInfos: List<RequestInfo>, durationInMillis: Long): RequestStat {
            var maxRespTime = Double.MIN_VALUE
            var minRespTime = Double.MAX_VALUE
            var avgRespTime = -1.0
            var p999RespTime = -1.0
            var p99RespTime = -1.0
            var sumRespTime = 0.0
            var count: Long = 0
            for ((_, respTime) in requestInfos) {
                ++count
                if (maxRespTime < respTime) {
                    maxRespTime = respTime
                }
                if (minRespTime > respTime) {
                    minRespTime = respTime
                }
                sumRespTime += respTime
            }
            if (count != 0L) {
                avgRespTime = sumRespTime / count
            }
            Collections.sort(requestInfos) { o1, o2 ->
                val diff = o1.responseTime - o2.responseTime
                when {
                    diff < 0.0 -> {
                        -1
                    }
                    diff > 0.0 -> {
                        1
                    }
                    else -> {
                        0
                    }
                }
            }
            val idx999 = (count * 0.999).toInt()
            val idx99 = (count * 0.99).toInt()
            if (count != 0L) {
                p999RespTime = requestInfos[idx999].responseTime
                p99RespTime = requestInfos[idx99].responseTime
            }
            val requestStat = RequestStat()
            requestStat.maxResponseTime = (maxRespTime)
            requestStat.minResponseTime = (minRespTime)
            requestStat.avgResponseTime = (avgRespTime)
            requestStat.p999ResponseTime = (p999RespTime)
            requestStat.p99ResponseTime = (p99RespTime)
            requestStat.count = (count)
            requestStat.tps = ((count / durationInMillis * 1000))
            return requestStat
        }
    }
}

data class RequestStat(
        var maxResponseTime: Double = 0.0,
        var minResponseTime: Double = 0.0,
        var avgResponseTime: Double = 0.0,
        var p999ResponseTime: Double = 0.0,
        var p99ResponseTime: Double = 0.0,
        var count: Long = 0,
        var tps: Long = 0
)