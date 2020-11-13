package br.meetingplace.server.dto.chat

import br.meetingplace.server.dto.Login

data class ChatSimpleOperator(var messageID: String, val receiver: ChatIdentifier, val login: Login)