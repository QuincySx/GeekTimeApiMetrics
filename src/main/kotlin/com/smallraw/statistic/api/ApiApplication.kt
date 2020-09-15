package com.smallraw.statistic.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ImportResource

@SpringBootApplication
@ImportResource(locations = ["classpath:config/application-bean.xml"])
class ApiApplication

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
