package br.meetingplace.server.dto.chat

import br.meetingplace.server.dto.Login

data class ChatComplexOperator(
        var message: String,
        var messageID: String,
        val source: ChatIdentifier,
        val receiver: ChatIdentifier,
        val login: Login
)