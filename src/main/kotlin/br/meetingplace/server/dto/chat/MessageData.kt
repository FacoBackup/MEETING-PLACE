package br.meetingplace.server.dto.chat

import br.meetingplace.server.dto.Login
import br.meetingplace.server.subjects.services.chat.dependencies.data.MessageType

data class MessageData(var message: String, val type: MessageType?, val receiver: ChatIdentifier, val login: Login)
