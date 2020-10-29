package br.meetingplace.management.services.chat.dependencies

import br.meetingplace.data.chat.ChatComplexOperations
import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.data.chat.ChatOperations
import br.meetingplace.management.dependencies.Generator
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteChat
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.chat.Message
import br.meetingplace.services.notification.Inbox

class ChatUser private constructor(): ChatInterface, ReadWriteUser, ReadWriteLoggedUser, ReadWriteChat, Verify, Generator {

    companion object{
        private val op = ChatUser()
        fun getChatUserOperator()= op
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
                    notification = Inbox("${user.social.getUserName()} sent a new message.", "Message.")

                    val existingChat = readChat(idChat)

                    chat.addMessage(msg)
                    writeChat(existingChat, idChat)

                    receiver.social.updateMyChats(idChat)
                    receiver.social.updateInbox(notification)
                    user.social.updateMyChats(idChat)

                    writeUser(user, user.getEmail())
                    writeUser(receiver, receiver.getEmail())

                }
                false -> { //The chat doesn't exist

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
            }
        }
    }//CREATE

    override fun favoriteMessage(data: ChatOperations){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val receiver = readUser(data.idReceiver)
        val idChat = getChatId(loggedUser, data.idReceiver)
        val chat = readChat(idChat)

        if(verifyChat(chat) && verifyLoggedUser(user) && verifyUser(receiver)){
            chat.favoriteMessage(data)
            writeChat(chat, idChat)
        }
    }//UPDATE

    override fun unFavoriteMessage(data: ChatOperations){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val receiver = readUser(data.idReceiver)
        val idChat = getChatId(loggedUser, data.idReceiver)
        val chat = readChat(idChat)

        if(verifyChat(chat) && verifyLoggedUser(user) && verifyUser(receiver)){
            chat.unFavoriteMessage(data)
            writeChat(chat, idChat)
        }
    }//UPDATE

    override fun quoteMessage(data: ChatComplexOperations){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val receiver = readUser(data.idReceiver)
        val idChat = getChatId(loggedUser, data.idReceiver)
        val chat = readChat(idChat)
        val notification = Inbox("${user.social.getUserName()} sent a new message.", "Message.")

        if(verifyChat(chat) && verifyLoggedUser(user) && verifyUser(receiver) && chat.verifyMessage(data.idMessage) ){
            chat.quoteMessage(data, generateId())
            writeChat(chat, idChat)
            receiver.social.updateInbox(notification)
            writeUser(receiver, receiver.getEmail())
        }
    }//UPDATE

    override fun shareMessage(data: ChatComplexOperations){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val receiver = readUser(data.idReceiver)
        val idChat =  getChatId(loggedUser, data.idSource)
        val chat = readChat(idChat)

        if(verifyChat(chat) && verifyLoggedUser(user) && verifyUser(receiver)){
            data.message = chat.shareMessage(data)
            if(data.message != ""){
                val sharedMessage = ChatMessage("|Shared| ${data.message}", data.idReceiver,true, data.creator,data.idCommunity)
                sendMessage(sharedMessage)
            }
        }
    } //UPDATE->CREATE

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