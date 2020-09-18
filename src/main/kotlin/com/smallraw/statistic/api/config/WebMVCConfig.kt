package com.smallraw.statistic.api.config

import com.smallraw.statistic.api.Interceptor.MetricsInterceptor
import com.smallraw.statistic.api.metrics.MetricsCollector
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebMVCConfig : WebMvcConfigurer {
    @Autowired
    lateinit var mMetrics: MetricsCollector

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(MetricsInterceptor(mMetrics))
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**")
    }
}