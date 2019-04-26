package com.shen.network

import com.shen.model.isSuccess
import network.HttpUtils
import org.apache.http.HttpResponse
import java.lang.Exception

class MessageUtil(private val number: String?) {
    private val host = "http://yzx.market.alicloudapi.com"
    private val path = "/yzx/sendSms"
    private val method = "POST"
    private val appcode = "e26e0d368a9a4e3b822388e34fcc101a"
    private val headers by lazy {
        HashMap<String, String>()
    }

    private val querys by lazy {
        HashMap<String, String>()
    }

    private val bodys by lazy {
        HashMap<String, String>()
    }

    fun sendMessage(): isSuccess {
        headers["Authorization"] = "APPCODE $appcode"
        querys["mobile"] = number ?: "13405665741"
        val code: Int = ((Math.random()*9 + 1) * 1000).toInt()
        querys["param"] = "code:$code"
        querys["tpl_id"] = "TP1710262"
        var isSuccess: isSuccess? = null
        try {
            val response: HttpResponse = HttpUtils
                .doPost(host, path, method, headers, querys, bodys)
            print(response.toString())
            isSuccess = isSuccess(true, code)
        } catch(e: Exception) {
            e.printStackTrace()
        }

        return isSuccess ?: isSuccess(false, errorReason = "发送验证码失败")
    }
}