package br.meetingplace.management.dependencies.verify.dependencies.chat

import br.meetingplace.services.chat.Chat

interface ChatVerifyInterface {
    fun verifyChat(chat: Chat): Boolean
}