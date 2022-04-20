package com.diettracker.notifications

data class DataMessage(
    var to: String,
    var notification: NotificationModel,
    var data: Map<String, String>
) {

}

data class NotificationModel(
    val title: String,
    val body: String,
)
