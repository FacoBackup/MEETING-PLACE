package br.meetingplace.server.subjects.entities.dependencies.services.chat

import br.meetingplace.server.subjects.services.chat.SimplifiedChat

interface UserChatInterface {
    fun updateMyChats(chat: SimplifiedChat)
    fun getMyChats(): List<SimplifiedChat>
}