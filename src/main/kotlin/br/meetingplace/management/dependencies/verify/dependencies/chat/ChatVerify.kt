package br.meetingplace.management.dependencies.verify.dependencies.chat

import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.services.chat.Chat

class ChatVerify private constructor(): ChatVerifyInterface{
    companion object{
        private val Class = ChatVerify()
        fun getClass ()= Class
    }

    override fun verifyChat(chat: Chat): Boolean {
        return chat.getConversationId().isNotBlank()
    }

}