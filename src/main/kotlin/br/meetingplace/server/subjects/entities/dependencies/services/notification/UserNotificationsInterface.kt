package br.meetingplace.server.subjects.entities.dependencies.services.notification

import br.meetingplace.server.subjects.services.notification.NotificationData

interface UserNotificationsInterface {
    fun updateInbox(notification: NotificationData)
    fun clearNotifications()
}