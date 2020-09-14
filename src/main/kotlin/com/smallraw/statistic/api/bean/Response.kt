package com.smallraw.statistic.api.bean

data class Response(
        val code: Int,
        val message: String,
        val data: Any?
) {
    companion object {
        fun success(msg: String, data: Any? = null): Response {
            return Response(200, msg, data)
        }

        fun failure(code: Int, msg: String, data: Any? = null): Response {
            return Response(code, msg, data)
        }
    }
}