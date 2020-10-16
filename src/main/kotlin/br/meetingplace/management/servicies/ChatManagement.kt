package br.meetingplace.management.servicies

import br.meetingplace.data.conversation.ChatContent
import br.meetingplace.data.conversation.ChatFullContent
import br.meetingplace.data.conversation.operations.ChatOperations
import br.meetingplace.interfaces.Generator
import br.meetingplace.management.GeneralManagement
import br.meetingplace.management.operations.ReadWrite.ReadWrite
import br.meetingplace.management.operations.verifiers.UserVerifiers
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.chat.Message
import br.meetingplace.servicies.notification.Inbox
import java.io.File

class ChatManagement: br.meetingplace.interfaces.Message, Generator{

    private val system = GeneralManagement.getManagement()
    private val management = system.getLoggedUser()
    private val verifier = UserVerifiers.getUserVerifier()
    private val generator = generateId()
    private val rw = ReadWrite.getRW()

    override fun sendMessage(message: ChatContent){

        val idChat = management + message.receiverId
        val fileUser = File("$management.json").exists()
        val fileReceiver = File("${message.receiverId}.json").exists()

        if(fileUser && fileReceiver && management != "" && message.receiverId != management ) {

            val user = rw.readUser(management)
            val receiver = rw.readUser(management)
            if (user.social.getChatIndex(idChat) == -1) { // the conversation doesn't exist
                val notification = Inbox("${user.social.getUserName()} started a conversation with you.", "Message.")
                val newChat = Chat(idChat)
                val content = Message(message.message, generator, message.static)
                newChat.addMessage(content)
                message.message = message.message + user.social.getUserName()

                receiver.social.startChat(newChat)
                user.social.startChat(newChat)
                receiver.social.updateInbox(notification)
            }
            else{ //the conversation exists

                val content = Message(message.message, generator, message.static)
                val notification = Inbox("${user.social.getUserName()} sent a new message.", "Message.")
                message.message = message.message + user.social.getUserName()

                receiver.social.newMessage(content, idChat)
                receiver.social.updateInbox(notification)
            }
        }
    }

    override fun deleteMessage(message: ChatOperations){

        val fileUser = File("$management.json").exists()
        if(fileUser && management != "" && verifier.verifyUserSocialProfile(management) && message.receiverId != management){
            val user = rw.readUser(management)
            val idChat = management + message.receiverId
            user.social.deleteMessage(message, idChat)
        }
    }

    override fun favoriteMessage(message: ChatOperations){

        val fileUser = File("$management.json").exists()
        if(fileUser && management != ""  && verifier.verifyUserSocialProfile(management) && message.receiverId != management){
            val idChat = management + message.receiverId
            val user = rw.readUser(management)
            user.social.favoriteMessage(message, idChat)
        }
    }

   override fun unFavoriteMessage(message: ChatOperations){

       val fileUser = File("$management.json").exists()
        if(fileUser && management !=  ""  && verifier.verifyUserSocialProfile(management) && message.receiverId != management){
            val user = rw.readUser(management)
            val idChat = management + message.receiverId

            user.social.unFavoriteMessage(message, idChat)
        }
    }

    override fun quoteMessage(message: ChatFullContent){

        val fileUser = File("$management.json").exists()
        if(fileUser && management !=  ""  && verifier.verifyUserSocialProfile(management) && message.receiverId!= management){

            val user = rw.readUser(management)
            val idChat = management + message.receiverId
            val newId = generator
            message.idNewMessage = newId
            println("LEVEL 1")
            user.social.quoteMessage(message, idChat)
        }
    }

    override fun shareMessage(content: ChatFullContent){ //NEEDS WORK

        val fileUser = File("$management.json").exists()
        if(fileUser && management !=  ""  && verifier.verifyUserSocialProfile(management) && content.receiverId!= management){

            val user = rw.readUser(management)
            val idChat = management + content.receiverId
            val operation = ChatOperations(content.idMessage, content.receiverId)
            val message = user.social.shareMessage(operation, idChat)

            if(message != ""){
                val sharedMessage = ChatContent(content.message, content.receiverId,true)
                sendMessage(sharedMessage)
            }
        }
    }


}