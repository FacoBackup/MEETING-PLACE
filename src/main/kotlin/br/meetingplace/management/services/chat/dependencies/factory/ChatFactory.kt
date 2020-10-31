package br.meetingplace.management.services.chat.dependencies.factory

import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.services.chat.Chat
import br.meetingplace.services.chat.Message
import br.meetingplace.services.notification.Inbox

class ChatFactory{

    private val iDs = IDsController.getClass()
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()
    companion object{
        private val Class = ChatFactory()
        fun getClass()= Class
    }

    fun createChat(data: ChatMessage){
        val logged = rw.readLoggedUser().fileName
        val user = rw.readUser(if(!logged.isNullOrBlank()) logged else "")
        lateinit var msg: Message
        lateinit var notification: Inbox
        lateinit var idChat: String
        lateinit var chat: Chat
        val receiver = rw.readUser(data.idReceiver)

        if(verify.verifyUser(user)&& verify.verifyUser(receiver) && verify.verifyChat(chat)){
            idChat = iDs.getChatId(logged!!, receiver.getEmail())
            chat = rw.readChat(idChat)
            when(verify.verifyChat(chat)){
                true->{
                    notification = Inbox("${user.getUserName()} started a conversation with you.", "Message.")
                    chat.startChat(listOf(logged, receiver.getEmail()), idChat)
                    msg = Message(data.message, iDs.generateId(), logged, true)
                    chat.addMessage(msg)

                    user.updateMyChats(idChat)
                    receiver.updateMyChats(idChat)
                    receiver.updateInbox(notification)

                    rw.writeChat(chat, idChat)
                    rw.writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
                    rw.writeUserToFile(receiver,iDs.attachNameToEmail(receiver.getUserName(),receiver.getEmail()))
                }
            }
        }
    }

}