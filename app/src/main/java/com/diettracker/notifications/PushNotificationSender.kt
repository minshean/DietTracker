package com.diettracker.notifications;

import android.util.Log

import okhttp3.ResponseBody;
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PushNotificationSender(
    private var fcmService : IFCMService?
) {

    fun sendNotificationToToken(token: String, text: String) {

//        val map = mapOf(
//            "title" to "New Order",
//            "body" to "Order Details",
//            "status" to "orderByUser",
//            "orderDate" to SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(System.currentTimeMillis()),
//            "orderId" to "dasddssadasd"
//        )

        val dataMessage = DataMessage(token, NotificationModel("New Review", text),
        mapOf(
            "title" to "New Review",
            "body" to text,
        ))

        if (fcmService != null) {
            fcmService!!.sendChatNotification(dataMessage)
                ?.enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {
                        try {
                            if (response.isSuccessful){
                                Log.d("112233","Notification sent successfully")
                            }
                            //Toast.makeText(AChat.this, "Failed!", Toast.LENGTH_SHORT).show();
                        } catch (e: Exception) {
                            Log.e("112233", e.toString(), e)
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Log.d("112233","Failed to send Notification")
                    }
                })
        }
    }

}