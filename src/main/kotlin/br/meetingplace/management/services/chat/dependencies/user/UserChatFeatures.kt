package br.meetingplace.management.services.chat.dependencies.user

import br.meetingplace.data.chat.ChatComplexOperations
import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.data.chat.ChatOperations
import br.meetingplace.management.dependencies.IDs
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteChat
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.management.services.chat.core.ChatCore
import br.meetingplace.management.services.chat.dependencies.ChatFeaturesInterface
import br.meetingplace.services.notification.Inbox

class UserChatFeatures private constructor(): ChatFeaturesInterface, ReadWriteUser, ReadWriteLoggedUser, ReadWriteChat, Verify, IDs{

    companion object{
        private val Class = UserChatFeatures()
        fun getClass()= Class
    }

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
        lateinit var notification: Inbox

        if(verifyChat(chat) && verifyLoggedUser(user) && verifyUser(receiver) && chat.verifyMessage(data.idMessage) ){
            notification = Inbox("${user.getUserName()} sent a new message.", "Message.")
            chat.quoteMessage(data, generateId())
            writeChat(chat, idChat)
            receiver.updateInbox(notification)
            writeUser(receiver, receiver.getEmail())
        }
    }//UPDATE

    override fun shareMessage(data: ChatComplexOperations){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val receiver = readUser(data.idReceiver)
        val idChat =  getChatId(loggedUser, data.idSource)
        val chat = readChat(idChat)
        lateinit var sharedMessage: ChatMessage

        if(verifyChat(chat) && verifyLoggedUser(user) && verifyUser(receiver)){
            data.message = chat.shareMessage(data)
            if(data.message != ""){
                sharedMessage = ChatMessage("|Shared| ${data.message}", data.idReceiver,true,data.idCommunity)
                ChatCore.getClass().sendMessage(sharedMessage)
            }
        }
    } //UPDATE->CREATE

}