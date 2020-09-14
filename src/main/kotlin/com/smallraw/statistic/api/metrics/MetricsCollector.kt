package com.smallraw.statistic.api.metrics

import java.io.Serializable


class MetricsCollector(private val metricsStorage: MetricsStorage) {
    //基于接口而非实现编程

    //用一个函数代替了最小原型中的两个函数
    fun recordRequest(requestInfo: RequestInfo) {
        if (requestInfo.apiName.isBlank()) {
            return
        }
        metricsStorage.saveRequestInfo(requestInfo)
    }

}

data class RequestInfo(
        var apiName: String,
        var responseTime: Double = 0.0,
        var timestamp: Long = 0
):Serializable