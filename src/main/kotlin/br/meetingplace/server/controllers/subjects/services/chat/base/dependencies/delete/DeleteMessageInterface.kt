package br.meetingplace.server.controllers.subjects.services.chat.base.dependencies.delete

import br.meetingplace.server.dto.chat.ChatSimpleOperator

interface DeleteMessageInterface {
    fun deleteUserMessage(data: ChatSimpleOperator)
    fun deleteGroupMessage(data: ChatSimpleOperator)
}