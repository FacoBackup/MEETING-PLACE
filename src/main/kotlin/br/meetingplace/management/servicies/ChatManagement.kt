package br.meetingplace.management.servicies

import br.meetingplace.data.conversation.ChatContent
import br.meetingplace.data.conversation.ChatFullContent
import br.meetingplace.data.conversation.operations.ChatOperations
import br.meetingplace.interfaces.file.ReadFile
import br.meetingplace.interfaces.file.WriteFile
import br.meetingplace.interfaces.utility.Generator
import br.meetingplace.interfaces.utility.Path
import br.meetingplace.interfaces.utility.Refresh
import br.meetingplace.interfaces.utility.Verifiers
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.chat.Message
import br.meetingplace.servicies.notification.Inbox

class ChatManagement private constructor(): ReadFile, WriteFile, Refresh, Generator, Path, Verifiers {

    companion object{
        private val chatManagement = ChatManagement()
        fun getManagement()= chatManagement
    }

    fun sendMessage(content: ChatContent){
        val management = readLoggedUser().email

        val idChat = getChatId(management, content.receiverId)
        if( verifyPath("users",management) &&  verifyPath("users",content.receiverId) && management != "" && content.receiverId != management && verifyUserSocialProfile(management)) {

            val user = readUser(management)
            val receiver = readUser(content.receiverId)


            if (!verifyPath("chats",idChat)) { // the conversation doesn't exist
                val notification = Inbox("${user.social.getUserName()} started a conversation with you.", "Message.")
                val newChat = Chat(idChat,listOf(management, content.receiverId))

                content.message = content.message + user.social.getUserName()
                val msg = Message(content.message, generateId(), management,true)
                newChat.addMessage(msg)


                user.social.updateMyChats(idChat)
                receiver.social.updateMyChats(idChat)
                receiver.social.updateInbox(notification)

                writeChat(idChat,newChat)
                writeUser(user.getEmail(), user)
                writeUser(receiver.getEmail(), receiver)
            }
            else{ //the conversation exists
                content.message = content.message + user.social.getUserName()
                val msg = Message(content.message, generateId(), management,true)
                val notification = Inbox("${user.social.getUserName()} sent a new message.", "Message.")
                val chat = readChat(idChat)

                chat.addMessage(msg)
                writeChat(idChat,chat)

                receiver.social.updateMyChats(idChat)
                receiver.social.updateInbox(notification)
                user.social.updateMyChats(idChat)
                writeUser(user.getEmail(), user)
                writeUser(receiver.getEmail(), receiver)

            }
        }
    }

    fun deleteMessage(message: ChatOperations){
        val management = readLoggedUser().email
        if (verifyPath("users",management) && verifyPath("users",message.receiverId) && management != ""
                && verifyUserSocialProfile(management) && message.receiverId != management){

            val user = readUser(management)
            val receiver = readUser(message.receiverId)
            val idChat = getChatId(management, message.receiverId)

            if(verifyPath("chats",idChat)) {
                val chat = readChat(idChat)
                chat.deleteMessage(message)
                writeChat(idChat,chat)
                writeUser(user.getEmail(), user)
                writeUser(receiver.getEmail(), receiver)
            }
        }
    }

    fun favoriteMessage(message: ChatOperations){
        val management = readLoggedUser().email

        if( verifyPath("users",management) &&  verifyPath("users",message.receiverId) && management != ""  && verifyUserSocialProfile(management)
                && message.receiverId != management){
            val user = readUser(management)
            val receiver = readUser(message.receiverId)
            val idChat = getChatId(management, message.receiverId)

            if(verifyPath("chats",idChat)) {
                val chat = readChat(idChat)
                chat.favoriteMessage(message)
                writeChat(idChat,chat)
                writeUser(user.getEmail(), user)
                writeUser(receiver.getEmail(), receiver)
            }
        }
    }

   fun unFavoriteMessage(message: ChatOperations){
       val management = readLoggedUser().email

        if( verifyPath("users",management) &&  verifyPath("users",message.receiverId) && management !=  ""
                && verifyUserSocialProfile(management) && message.receiverId != management){
            val user = readUser(management)
            val receiver = readUser(message.receiverId)
            val idChat = getChatId(management, message.receiverId)


            if(verifyPath("chats",idChat)) {
                val chat = readChat(idChat)
                chat.unFavoriteMessage(message)
                writeChat(idChat,chat)
                writeUser(user.getEmail(), user)
                writeUser(receiver.getEmail(), receiver)
            }
        }
   }
    fun quoteMessage(message: ChatFullContent){
        val management = readLoggedUser().email

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
                writeUser(user.getEmail(), user)
                writeUser(receiver.getEmail(), receiver)
            }
        }
    }

    fun shareMessage(content: ChatFullContent){ //NEEDS WORK
        val management = readLoggedUser().email

        val idChat = management + content.receiverId

        if( verifyPath("users", management) && management !=  "" &&  verifyPath("users", content.receiverId) && verifyUserSocialProfile(management)
                && content.receiverId!= management && verifyPath("chats", idChat)){

            val operation = ChatOperations(content.idMessage, content.receiverId)
            val chat = readChat(idChat)
            val message = chat.shareMessage(operation)

            if(message != "" && content.receiverId !in chat.getOwners()){
                val sharedMessage = ChatContent(message, content.receiverId,true)
                sendMessage(sharedMessage)
            }
        }
    }
}