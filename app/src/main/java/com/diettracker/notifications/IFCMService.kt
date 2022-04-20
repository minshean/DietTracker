package com.diettracker.notifications

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

const val FCM_AUTH_KEY = "AAAAZnn9qrM:APA91bHwrQVQVi54gwiyV_KQnJ1x9xGPt-xX2ewtugp01fCw-molcbk6UJxrGv0nBrbttUoqqY1idpo_Egj7QuHHkpMt_RMzaGC4Eaf_tpXtpGvqEaYhUefzNfeQdo7qTAyZDmjJJobU"

interface IFCMService {

    @Headers("Authorization: key=$FCM_AUTH_KEY",
        "Content-Type:application/json")
    @POST("fcm/send")
    fun sendChatNotification(@Body dataMessage: DataMessage?): Call<ResponseBody?>?
}