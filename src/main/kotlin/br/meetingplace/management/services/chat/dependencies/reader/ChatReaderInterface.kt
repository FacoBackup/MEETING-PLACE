package br.meetingplace.management.services.chat.dependencies.reader

import br.meetingplace.data.Data

interface ChatReaderInterface {
    fun seeChat(data: Data)
    fun seeGroupChat(data: Data)
}