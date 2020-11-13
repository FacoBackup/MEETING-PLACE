package br.meetingplace.server.controllers.subjects.services.chat.extensions.dependencies.quote

import br.meetingplace.server.dto.chat.ChatComplexOperator

interface QuoteMessageInterface {
    fun userQuoteMessage(data: ChatComplexOperator)
    fun groupQuoteMessage(data: ChatComplexOperator)
}