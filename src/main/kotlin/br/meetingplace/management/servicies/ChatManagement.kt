package br.meetingplace.management.servicies

import br.meetingplace.data.conversation.ChatContent
import br.meetingplace.data.conversation.ChatFullContent
import br.meetingplace.data.conversation.operations.ChatOperations
import br.meetingplace.management.GeneralManagement
import br.meetingplace.management.operations.finders.UserFinders
import br.meetingplace.management.operations.verifiers.UserVerifiers
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.chat.Message
import br.meetingplace.servicies.notification.Inbox
import io.ktor.utils.io.*

open class ChatManagement{
    private val management = GeneralManagement.getLoggedUser()
    /*
    fun sendMessage(message: ChatContent){

        val finder = UserFinders.getUserFinder()
        val verifier = UserVerifiers.getUserVerifier()
        val indexUser = finder.getUserIndex(management)
        val indexReceiver = finder.getUserIndex(message.receiverId)
        val idChat = management + message.receiverId

        if(management != "" && verifier.verifyUser(management) && indexReceiver != -1 && message.receiverId != management ) {
            if (userList[indexUser].social.getChatIndex(idChat) == -1) { // the conversation doesn't exist
                val notification = Inbox("${userList[indexUser].social.getUserName()} started a conversation with you.", "Message.")
                val newChat = Chat(idChat)
                val content = Message(message.message, generateMessageId(userList[indexUser].social.getChatById(idChat)), message.static)
                newChat.addMessage(content)
                message.message = message.message + userList[indexUser].social.getUserName()

                userList[indexReceiver].social.startChat(newChat)
                userList[indexUser].social.startChat(newChat)
                userList[indexReceiver].social.updateInbox(notification)
            }
            else{ //the conversation exists

                val content = Message(message.message, generateMessageId(userList[indexUser].social.getChatById(idChat)), message.static)
                val notification = Inbox("${userList[indexUser].social.getUserName()} sent a new message.", "Message.")
                message.message = message.message + userList[indexUser].social.getUserName()

                userList[indexReceiver].social.newMessage(content, idChat)
                userList[indexReceiver].social.updateInbox(notification)
            }
        }
    }

    fun deleteMessage(message: ChatOperations){

        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser()) && message.receiver != getLoggedUser()){

            val indexUser = getUserIndex(getLoggedUser())
            val idChat = getLoggedUser() + message.receiver


            userList[indexUser].social.deleteMessage(message, idChat)
        }
    }

    fun favoriteMessage(message: ChatOperations){
        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser()) && message.receiver != getLoggedUser()){
            val indexUser = getUserIndex(getLoggedUser())
            val idChat = getLoggedUser() + message.receiver

            userList[indexUser].social.favoriteMessage(message, idChat)
        }
    }

    fun unFavoriteMessage(message: ChatOperations){
        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser()) && message.receiver != getLoggedUser()){
            val indexUser = getUserIndex(getLoggedUser())
            val idChat = getLoggedUser() + message.receiver

            userList[indexUser].social.unFavoriteMessage(message, idChat)
        }
    }

    fun quoteMessage(message: ChatFullContent){
        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser()) && message.receiver!= getLoggedUser()){
            val indexUser = getUserIndex(getLoggedUser())
            val idChat = getLoggedUser() + message.receiver
            val newId = generateMessageId(userList[indexUser].social.getChatById(idChat))
            message.idNewMessage = newId
            println("LEVEL 1")
            userList[indexUser].social.quoteMessage(message, idChat)
        }
    }

    fun shareMessage(content: ChatFullContent){ //NEEDS WORK

        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser()) && content.receiver!= getLoggedUser()){
            val indexUser = getUserIndex(getLoggedUser())
            val idChat = getLoggedUser() + content.receiver
            val operation = ChatOperations(content.idMessage, content.receiver)
            val message = userList[indexUser].social.shareMessage(operation, idChat)

            if(message != ""){
                val sharedMessage = ChatContent(content.message, content.receiver,true)
                sendMessage(sharedMessage)
            }
        }
    }

     */
}