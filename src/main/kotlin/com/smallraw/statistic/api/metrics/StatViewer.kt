package com.smallraw.statistic.api.metrics

import com.google.gson.Gson
import java.util.*


interface StatViewer {
    fun output(requestStats: Map<String, RequestStat>, startTimeInMillis: Long, endTimeInMills: Long)
}

class ConsoleViewer : StatViewer {
    override fun output(
            requestStats: Map<String, RequestStat>, startTimeInMillis: Long, endTimeInMills: Long) {
        println("Time Span: [$startTimeInMillis, $endTimeInMills]")
        val gson = Gson()
        System.out.println(gson.toJson(requestStats))
    }
}

class EmailSender {

}

class EmailViewer : StatViewer {
    private var emailSender: EmailSender
    private val toAddresses: MutableList<String> = ArrayList()

    constructor() {
        emailSender = EmailSender()
    }

    constructor(emailSender: EmailSender) {
        this.emailSender = emailSender
    }

    fun addToAddress(address: String) {
        toAddresses.add(address)
    }

    override fun output(
            requestStats: Map<String, RequestStat>, startTimeInMillis: Long, endTimeInMills: Long) {
        // format the requestStats to HTML style.
        // send it to email toAddresses.
    }
}