package br.meetingplace.management.services.chat.dependencies.reader

import br.meetingplace.data.Data
import br.meetingplace.services.chat.Chat

interface ChatReaderInterface {
    fun seeChat(data: Data): Chat?
}