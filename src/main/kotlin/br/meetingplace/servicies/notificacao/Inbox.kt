package br.meetingplace.servicies.notificacao

class Inbox(Notification: String, Type: String) {

    private var notification = Notification
    private var type = Type

    fun getNotification() = notification
    fun getType() = type
}