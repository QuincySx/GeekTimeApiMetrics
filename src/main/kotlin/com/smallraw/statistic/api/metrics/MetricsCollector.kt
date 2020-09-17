package com.smallraw.statistic.api.metrics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.io.Serializable


class MetricsCollector(private val metricsStorage: MetricsStorage = MemoryMetricsStorage()) : CoroutineScope by MainScope() {
    private val mChannel = Channel<RequestInfo>(256)

    init {
        launch(Dispatchers.IO) {
            while (true) {
                val requestInfo = mChannel.receive()
                metricsStorage.saveRequestInfo(requestInfo)
            }
        }
    }

    //基于接口而非实现编程
    //用一个函数代替了最小原型中的两个函数
    fun recordRequest(requestInfo: RequestInfo) {
        if (requestInfo.apiName.isBlank()) {
            return
        }
        launch(Dispatchers.IO) {
            mChannel.send(requestInfo)
        }
    }

}

data class RequestInfo(
        var apiName: String,
        var responseTime: Double = 0.0,
        var timestamp: Long = 0
) : Serializable