package br.meetingplace.server.controllers.dependencies.newRW.chat

import br.meetingplace.server.subjects.services.chat.Chat

interface ChatRWInterface {
    fun read(id: String): Chat
    fun write(data: Chat)
    fun delete(data: Chat)
}