package br.meetingplace.server.controllers.subjects.services.chat.extensions.dependencies.share

import br.meetingplace.server.dto.chat.ChatComplexOperator

interface ShareMessageInterface {
    fun userShareMessage(data: ChatComplexOperator)
    fun groupShareMessage(data: ChatComplexOperator)
}