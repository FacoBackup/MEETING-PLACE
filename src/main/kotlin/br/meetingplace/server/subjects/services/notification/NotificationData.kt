package br.meetingplace.server.subjects.services.notification

import br.meetingplace.server.subjects.services.notification.data.NotificationMainType
import br.meetingplace.server.subjects.services.notification.data.NotificationSubType

data class NotificationData(val mainType: NotificationMainType, val subType: NotificationSubType)