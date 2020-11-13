package br.meetingplace.server.subjects.entities.dependencies.services.chat

import br.meetingplace.server.subjects.services.chat.SimplifiedChat

class UserChat private constructor() : UserChatInterface {

    private var myChats = mutableListOf<SimplifiedChat>()

    companion object {
        private val Class = UserChat()
        fun getClass() = Class
    }

    override fun updateMyChats(chat: SimplifiedChat) {
        if (chat !in myChats)
            myChats.add(chat)
    }

    override fun getMyChats() = myChats
}