package br.meetingplace.management.servicies

import br.meetingplace.data.conversation.ChatContent
import br.meetingplace.data.conversation.ChatFullContent
import br.meetingplace.data.conversation.operations.ChatOperations
import br.meetingplace.management.GeneralManagement
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.chat.Message
import br.meetingplace.servicies.notification.Inbox
import io.ktor.utils.io.*

open class ChatManagement: GeneralManagement() {

    fun sendMessage(message: ChatContent){

        val indexUser = getUserIndex(getLoggedUser())
        val indexReceiver = getUserIndex(message.receiver)
        val idChat = getLoggedUser() + message.receiver

        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser()) && indexReceiver != -1 ) {
            if (userList[indexUser].social.getChatIndex(idChat) == -1) { // the conversation doesn't exist
                val notification = Inbox("${userList[indexUser].social.getUserName()} started a conversation with you.", "Message.")
                val newChat = Chat(idChat)
                val content = Message(message.message, generateMessageId(userList[indexUser].social.getChatById(idChat)), message.static)
                newChat.addMessage(content)
                userList[indexReceiver].social.startChat(newChat)
                userList[indexUser].social.startChat(newChat)
                userList[indexReceiver].social.updateInbox(notification)
            }
            else{ //the conversation exists

                val content = Message(message.message, generateMessageId(userList[indexUser].social.getChatById(idChat)), message.static)
                val notification = Inbox("${userList[indexUser].social.getUserName()} sent a new message.", "Message.")
                message.message = userList[indexUser].social.getUserName() + message.message

                userList[indexReceiver].social.newMessage(content, getLoggedUser())
                userList[indexReceiver].social.updateInbox(notification)
            }
        }
    }

    fun deleteMessage(message: ChatOperations){

        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser())){

            val indexUser = getUserIndex(getLoggedUser())
            val idChat = getLoggedUser() + message.receiver

            userList[indexUser].social.deleteMessage(message, idChat)
        }
    }

    fun favoriteMessage(message: ChatOperations){
        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser())){
            val indexUser = getUserIndex(getLoggedUser())
            val idChat = getLoggedUser() + message.receiver

            userList[indexUser].social.favoriteMessage(message, idChat)
        }
    }

    fun unFavoriteMessage(message: ChatOperations){
        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser())){
            val indexUser = getUserIndex(getLoggedUser())
            val idChat = getLoggedUser() + message.receiver

            userList[indexUser].social.unFavoriteMessage(message, idChat)
        }
    }

    fun quoteMessage(message: ChatFullContent){
        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser())){
            val indexUser = getUserIndex(getLoggedUser())
            val idChat = getLoggedUser() + message.receiver

            userList[indexUser].social.quoteMessage(message, idChat)
        }
    }

    fun shareMessage(operations: ChatOperations){ //NEEDS WORK
        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser())){
            val indexUser = getUserIndex(getLoggedUser())
            val idChat = getLoggedUser() + operations.receiver

            val message = userList[indexUser].social.shareMessage(operations, idChat)

            if(message != ""){
                sendMessage()
            }
        }
    }
}