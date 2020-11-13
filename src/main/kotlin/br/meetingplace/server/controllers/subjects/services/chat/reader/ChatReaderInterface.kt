package br.meetingplace.server.controllers.subjects.services.chat.reader

import br.meetingplace.server.dto.chat.ChatFinderOperator
import br.meetingplace.server.subjects.services.chat.Chat

interface ChatReaderInterface {
    fun seeChat(data: ChatFinderOperator): Chat?
}