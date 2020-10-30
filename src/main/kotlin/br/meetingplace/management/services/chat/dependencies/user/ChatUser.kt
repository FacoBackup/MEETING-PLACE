package br.meetingplace.management.services.chat.dependencies.user

import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.data.chat.ChatOperations
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteChat
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.management.services.chat.dependencies.BaseChatInterface
import br.meetingplace.management.services.chat.dependencies.factory.ChatFactory
import br.meetingplace.services.chat.Message
import br.meetingplace.services.notification.Inbox

class ChatUser private constructor(): BaseChatInterface, ReadWriteUser, ReadWriteLoggedUser, ReadWriteChat, Verify, Generator {

    companion object{
        private val Class = ChatUser()
        fun getClass()= Class
    }

    override fun sendMessage(data: ChatMessage){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val receiver = readUser(data.idReceiver)
        val idChat = getChatId(loggedUser, receiver.getEmail())
        val chat = readChat(idChat)
        lateinit var msg: Message
        lateinit var notification: Inbox

        if(verifyLoggedUser(user) && verifyUser(receiver)) {
            when (verifyChat(chat)) {
                true -> { //The chat exists
                    msg = Message(data.message, generateId(), loggedUser, true)
                    notification = Inbox("${user.getUserName()} sent a new message.", "Message.")

                    val existingChat = readChat(idChat)

                    chat.addMessage(msg)
                    writeChat(existingChat, idChat)

                    receiver.updateMyChats(idChat)
                    receiver.updateInbox(notification)
                    user.updateMyChats(idChat)

                    writeUser(user, user.getEmail())
                    writeUser(receiver, receiver.getEmail())

                }
                false -> { //The chat doesn't exist
                    ChatFactory.getClass().createChat(data)
                }
            }
        }
    }//CREATE

    override fun deleteMessage(data: ChatOperations){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val receiver = readUser(data.idReceiver)
        val idChat = getChatId(loggedUser, data.idReceiver)
        val chat = readChat(idChat)

        if (verifyLoggedUser(user) && verifyUser(receiver) && verifyChat(chat)){
            chat.deleteMessage(data)
            writeChat(chat, idChat)
            writeUser(user, user.getEmail())
            writeUser(receiver, receiver.getEmail())
        }
    }//DELETE
}