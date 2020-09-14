package com.smallraw.statistic.api.global

import com.smallraw.statistic.api.bean.Response
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor
import org.springframework.web.bind.annotation.*


@ControllerAdvice
@Component
@RestController
class GlobalExceptionHandler {
    @Bean
    fun methodValidationPostProcessor(): MethodValidationPostProcessor {
        return MethodValidationPostProcessor()
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: Exception): Response {
        return Response.failure(400, exception.message ?: "Unknown")
    }
}