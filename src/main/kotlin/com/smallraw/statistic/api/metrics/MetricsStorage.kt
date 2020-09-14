package com.smallraw.statistic.api.metrics

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import java.io.Serializable
import kotlin.streams.toList


interface MetricsStorage {
    fun saveRequestInfo(requestInfo: RequestInfo)
    fun getRequestInfos(apiName: String, startTimeInMillis: Long, endTimeInMillis: Long): List<RequestInfo>
    fun getRequestInfos(startTimeInMillis: Long, endTimeInMillis: Long): Map<String, List<RequestInfo>>
}

class RedisMetricsStorage : MetricsStorage {
    @Autowired
    private lateinit var serializableRedisTemplate: RedisTemplate<String, Serializable>

    //...省略属性和构造函数等...
    override fun saveRequestInfo(requestInfo: RequestInfo) {
        val get = serializableRedisTemplate.opsForHash<String, List<RequestInfo>>().get("api", requestInfo.apiName)
                ?: arrayListOf()
        val list = get + requestInfo
        serializableRedisTemplate.opsForHash<String, List<RequestInfo>>().put("api", requestInfo.apiName, list)
    }

    override fun getRequestInfos(apiName: String, startTimestamp: Long, endTimestamp: Long): List<RequestInfo> {
        val redisValues = serializableRedisTemplate.opsForHash<String, List<RequestInfo>>().get("api", apiName)
                ?: arrayListOf()
        return redisValues.parallelStream().filter {
            it.timestamp in startTimestamp..endTimestamp
        }.toList()
    }

    override fun getRequestInfos(startTimestamp: Long, endTimestamp: Long): Map<String, List<RequestInfo>> {
        val redisValues = serializableRedisTemplate.opsForHash<String, List<RequestInfo>>().values("api")
        val result = HashMap<String, List<RequestInfo>>()
        redisValues.forEach {
            val resultList = it.parallelStream().filter { requestInfo ->
                requestInfo.timestamp in startTimestamp..endTimestamp
            }.toList()
            if (resultList.isNotEmpty()) {
                result[resultList[0].apiName] = resultList
            }
        }
        return result
    }
}