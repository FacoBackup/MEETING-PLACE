package br.meetingplace.management.services.chat.dependencies.factory

import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.verify.dependencies.Verify
import br.meetingplace.management.dependencies.readwrite.dependencies.chat.ReadWriteChat
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteUser
import br.meetingplace.services.chat.Message
import br.meetingplace.services.notification.Inbox

class ChatFactory: Verify, ReadWriteUser, ReadWriteChat, ReadWriteLoggedUser {
    private val iDs = IDsController.getClass()
    companion object{
        private val Class = ChatFactory()
        fun getClass()= Class
    }

    fun createChat(data: ChatMessage){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val receiver = readUser(data.idReceiver)
        val idChat = iDs.getChatId(loggedUser, receiver.getEmail())
        val chat = readChat(idChat)
        lateinit var msg: Message
        lateinit var notification: Inbox

        if(verifyChat(chat))
        notification = Inbox("${user.getUserName()} started a conversation with you.", "Message.")
        chat.startChat(listOf(loggedUser, receiver.getEmail()), idChat)
        msg = Message(data.message, iDs.generateId(), loggedUser, true)
        chat.addMessage(msg)

        user.updateMyChats(idChat)
        receiver.updateMyChats(idChat)
        receiver.updateInbox(notification)

        writeChat(chat, idChat)
        writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
        writeUserToFile(receiver,iDs.attachNameToEmail(receiver.getUserName(),receiver.getEmail()))
    }

}