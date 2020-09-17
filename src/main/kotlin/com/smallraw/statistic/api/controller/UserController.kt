package com.smallraw.statistic.api.controller

import com.smallraw.statistic.api.bean.Response
import com.smallraw.statistic.api.bean.UserDTO
import com.smallraw.statistic.api.bean.UserVo
import com.smallraw.statistic.api.metrics.MetricsCollector
import com.smallraw.statistic.api.metrics.RequestInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.SecureRandom
import java.util.concurrent.atomic.AtomicLong
import javax.validation.constraints.Size


@RestController
class UserController {
    @Autowired
    lateinit var mMetrics: MetricsCollector

    val content = AtomicLong(1000)

    @RequestMapping("/login", method = [RequestMethod.GET])
    fun login(
            @RequestParam(value = "name", required = true) @Size(max = 10) name: String,
            @RequestParam(value = "password", required = true) @Size(max = 10) password: String
    ): Response {
        val startTimestamp = System.currentTimeMillis()
        return Response.success("success", UserDTO(content.addAndGet(1), name)).also {
            val respTime = System.currentTimeMillis() - startTimestamp
            mMetrics.recordRequest(RequestInfo("login", respTime.toDouble(), startTimestamp))
        }
    }

    @RequestMapping("/register", method = [RequestMethod.GET, RequestMethod.POST])
    fun register(user: UserVo): Response {
        val startTimestamp = System.currentTimeMillis()
        Thread.sleep(SecureRandom().nextInt(100).toLong())
        return Response.success("success", UserDTO(content.addAndGet(1), user.name)).also {
            val respTime = System.currentTimeMillis() - startTimestamp
            mMetrics.recordRequest(RequestInfo("regsiter", respTime.toDouble(), startTimestamp))
        }
    }
}