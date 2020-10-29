package br.meetingplace.management.services.chat.dependencies.factory

import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.management.dependencies.Generator
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteChat
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.management.services.chat.dependencies.user.ChatUser
import br.meetingplace.services.chat.Message
import br.meetingplace.services.notification.Inbox

class ChatFactory: Verify, ReadWriteUser, ReadWriteChat, ReadWriteLoggedUser, Generator{

    companion object{
        private val Class = ChatFactory()
        fun getClass()= Class
    }

    fun createChat(data: ChatMessage){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val receiver = readUser(data.idReceiver)
        val idChat = getChatId(loggedUser, receiver.getEmail())
        val chat = readChat(idChat)
        lateinit var msg: Message
        lateinit var notification: Inbox

        if(verifyChat(chat))
        notification = Inbox("${user.social.getUserName()} started a conversation with you.", "Message.")
        chat.startChat(listOf(loggedUser, receiver.getEmail()), idChat)
        msg = Message(data.message, generateId(), loggedUser, true)
        chat.addMessage(msg)

        user.social.updateMyChats(idChat)
        receiver.social.updateMyChats(idChat)
        receiver.social.updateInbox(notification)

        writeChat(chat, idChat)
        writeUser(user, user.getEmail())
        writeUser(receiver, receiver.getEmail())
    }

    fun deleteChat() {
        TODO("Not yet implemented")
    }
}