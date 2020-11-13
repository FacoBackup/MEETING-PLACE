package br.meetingplace.server.controllers.subjects.services.chat.base.dependencies.send

import br.meetingplace.server.dto.chat.MessageData

interface SendMessageInterface {
    fun sendUserMessage(data: MessageData)
    fun sendGroupMessage(data: MessageData)
}