package br.meetingplace.server.controllers.subjects.services.chat.factory

import br.meetingplace.server.dto.chat.MessageData

interface ChatFactoryInterface {
    fun createChat(data: MessageData)
}