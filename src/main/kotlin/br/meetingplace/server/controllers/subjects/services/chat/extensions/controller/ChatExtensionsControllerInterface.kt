package br.meetingplace.server.controllers.subjects.services.chat.extensions.controller

import br.meetingplace.server.dto.chat.ChatComplexOperator
import br.meetingplace.server.dto.chat.ChatSimpleOperator

interface ChatExtensionsControllerInterface {
    fun favoriteMessage(data: ChatSimpleOperator)
    fun disfavorMessagr(data: ChatSimpleOperator)
    fun quoteMessage(data: ChatComplexOperator)
    fun shareMessage(data: ChatComplexOperator)
}