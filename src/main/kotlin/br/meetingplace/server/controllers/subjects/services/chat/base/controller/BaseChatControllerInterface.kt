package br.meetingplace.server.controllers.subjects.services.chat.base.controller

import br.meetingplace.server.dto.chat.ChatSimpleOperator
import br.meetingplace.server.dto.chat.MessageData

interface BaseChatControllerInterface {
    fun sendMessage(data: MessageData)
    fun deleteMessage(data: ChatSimpleOperator)
}