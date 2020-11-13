package br.meetingplace.server.controllers.subjects.services.chat.extensions.dependencies.disfavor

import br.meetingplace.server.dto.chat.ChatSimpleOperator

interface DisfavorMessageInterface {
    fun userDisfavorMessage(data: ChatSimpleOperator)
    fun groupDisfavorMessage(data: ChatSimpleOperator)
}