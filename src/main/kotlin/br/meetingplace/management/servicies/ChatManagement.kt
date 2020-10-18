package br.meetingplace.management.servicies

import br.meetingplace.data.conversation.ChatContent
import br.meetingplace.data.conversation.ChatFullContent
import br.meetingplace.data.conversation.operations.ChatOperations
import br.meetingplace.interfaces.*
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.chat.Message
import br.meetingplace.servicies.notification.Inbox
import java.io.File

class ChatManagement private constructor(): ReadFile, WriteFile, Refresh, br.meetingplace.interfaces.Message, Generator, Path, Verifiers{

    companion object{
        private val chatManagement = ChatManagement()
        fun getManagement()= chatManagement
    }

    fun sendMessage(content: ChatContent){
        val log = refreshData()
        val management = log.user

        val idChat = getChatId(management, content.receiverId)
        if( verifyPath("users",management) &&  verifyPath("users",content.receiverId) && management != "" && content.receiverId != management && verifyUserSocialProfile(management)) {

            val user = readUser(management)
            val receiver = readUser(content.receiverId)
            println(receiver.social.getUserName())


            if (!verifyPath("chats",idChat)) { // the conversation doesn't exist
                val notification = Inbox("${user.social.getUserName()} started a conversation with you.", "Message.")
                val newChat = Chat(idChat,listOf(management, content.receiverId))

                content.message = content.message + user.social.getUserName()
                val msg = Message(content.message, generateId(), true)
                newChat.addMessage(msg)


                user.social.updateMyChats(idChat)
                receiver.social.updateMyChats(idChat)
                receiver.social.updateInbox(notification)

                writeChat(idChat,newChat)
                writeUser(user.getId(), user)
                writeUser(receiver.getId(), receiver)
            }
            else{ //the conversation exists
                content.message = content.message + user.social.getUserName()
                val msg = Message(content.message, generateId(), content.static)
                val notification = Inbox("${user.social.getUserName()} sent a new message.", "Message.")
                val chat = readChat(idChat)

                chat.addMessage(msg)
                writeChat(idChat,chat)

                receiver.social.updateMyChats(idChat)
                receiver.social.updateInbox(notification)
                user.social.updateMyChats(idChat)
                writeUser(user.getId(), user)
                writeUser(receiver.getId(), receiver)

            }
        }
    }

    fun deleteMessage(message: ChatOperations){
        val log = refreshData()
        val management = log.user

        if (verifyPath("users",management) && verifyPath("users",message.receiverId) && management != ""
                && verifyUserSocialProfile(management) && message.receiverId != management){

            val user = readUser(management)
            val receiver = readUser(message.receiverId)
            val idChat = getChatId(management, message.receiverId)

            if(verifyPath("chats",idChat)) {
                val chat = readChat(idChat)
                chat.deleteMessage(message)
                writeChat(idChat,chat)
                writeUser(user.getId(), user)
                writeUser(receiver.getId(), receiver)
            }
        }
    }

    fun favoriteMessage(message: ChatOperations){
        val log = refreshData()
        val management = log.user

        if( verifyPath("users",management) &&  verifyPath("users",message.receiverId) && management != ""  && verifyUserSocialProfile(management)
                && message.receiverId != management){
            val user = readUser(management)
            val receiver = readUser(message.receiverId)
            val idChat = getChatId(management, message.receiverId)

            if(verifyPath("chats",idChat)) {
                val chat = readChat(idChat)
                chat.favoriteMessage(message)
                writeChat(idChat,chat)
                writeUser(user.getId(), user)
                writeUser(receiver.getId(), receiver)
            }
        }
    }

   fun unFavoriteMessage(message: ChatOperations){
       val log = refreshData()
       val management = log.user

        if( verifyPath("users",management) &&  verifyPath("users",message.receiverId) && management !=  ""
                && verifyUserSocialProfile(management) && message.receiverId != management){
            val user = readUser(management)
            val receiver = readUser(message.receiverId)
            val idChat = getChatId(management, message.receiverId)


            if(verifyPath("chats",idChat)) {
                val chat = readChat(idChat)
                chat.unFavoriteMessage(message)
                writeChat(idChat,chat)
                writeUser(user.getId(), user)
                writeUser(receiver.getId(), receiver)
            }
        }
   }
    fun quoteMessage(message: ChatFullContent){
        val log = refreshData()
        val management = log.user

        if( verifyPath("users",management) &&  verifyPath("users",message.receiverId) && management !=  ""
                && verifyUserSocialProfile(management) && message.receiverId!= management){

            val user = readUser(management)
            val receiver = readUser(message.receiverId)
            val idChat = getChatId(management, message.receiverId)
            val newId = generateId()
            message.idNewMessage = newId

            if(verifyPath("chats",idChat)) {
                val chat = readChat(idChat)
                chat.quoteMessage(message)
                writeChat(idChat,chat)
                writeUser(user.getId(), user)
                writeUser(receiver.getId(), receiver)
            }
        }
    }

    /*
    override fun shareMessage(content: ChatFullContent){ //NEEDS WORK

        val fileUser = File("$management.json").exists()
        if(fileUser && management !=  ""  && verifyUserSocialProfile(management) && content.receiverId!= management){

            val user = readUser(management)
            val idChat = management + content.receiverId
            val operation = ChatOperations(content.idMessage, content.receiverId)
            val message = user.social.shareMessage(operation, idChat)

            if(message != ""){
                val sharedMessage = ChatContent(content.message, content.receiverId,true)
                sendMessage(sharedMessage)
            }
        }
    }

     */


}