package com.smallraw.statistic.api.Interceptor

import com.smallraw.statistic.api.metrics.MetricsCollector
import com.smallraw.statistic.api.metrics.RequestInfo
import org.springframework.core.NamedThreadLocal
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class MetricsInterceptor(private val metrics: MetricsCollector) : HandlerInterceptor {
    private val threadLocal = NamedThreadLocal<Long>("PerformanceMonitor")

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        //请求开始时间
        val startTime = System.currentTimeMillis()
        //线程绑定变量（该数据只有当前请求的线程可见）
        threadLocal.set(startTime)
        return super.preHandle(request, response, handler)
    }

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
        super.postHandle(request, response, handler, modelAndView)
        //获取线程绑定的局部变量（开始时间）
        val startTimestamp = threadLocal.get()
        val respTime = System.currentTimeMillis() - startTimestamp
        metrics.recordRequest(RequestInfo(request.requestURI, respTime.toDouble(), startTimestamp))
    }
}