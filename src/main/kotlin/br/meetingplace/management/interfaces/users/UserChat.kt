package br.meetingplace.management.interfaces.users

import br.meetingplace.data.chat.ChatComplexOperations
import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.data.chat.ChatOperations
import br.meetingplace.interfaces.file.ReadFile
import br.meetingplace.interfaces.file.WriteFile
import br.meetingplace.interfaces.utility.Generator
import br.meetingplace.interfaces.utility.Path
import br.meetingplace.interfaces.utility.Refresh
import br.meetingplace.interfaces.utility.Verifiers
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.chat.Message
import br.meetingplace.servicies.notification.Inbox

interface UserChat: ReadFile, WriteFile, Refresh, Generator, Path, Verifiers {

    fun sendMessageUser(content: ChatMessage){
        val management = readLoggedUser().email

        val idChat = getChatId(management, content.id)
        if( verifyPath("users",management) &&  verifyPath("users",content.id) && management != "" && content.id != management && verifyUserSocialProfile()) {

            val user = readUser(management)
            val receiver = readUser(content.id)


            if (!verifyPath("chats",idChat)) { // the conversation doesn't exist
                val notification = Inbox("${user.social.getUserName()} started a conversation with you.", "Message.")
                val newChat = Chat(idChat,listOf(management, content.id))

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
    }//CREATE
    fun getMyChats(): MutableList<Chat> {
        val management = readLoggedUser().email

        val chats = mutableListOf<Chat>()
        if( verifyPath("users",management)){
            val user = readUser(management)

            val userChats = user.social.getMyChats()
            for(i in 0 until userChats.size){
                if (verifyPath("chats",userChats[i])){
                    val chat = readChat(userChats[i])
                    chats.add(chat)
                }
            }
            return chats
        }
        return chats
    }//READ
    fun favoriteMessageUser(message: ChatOperations){
        val management = readLoggedUser().email

        if( verifyPath("users",management) &&  verifyPath("users",message.id) && management != ""  && verifyUserSocialProfile()
                && message.id != management){
            val idChat = getChatId(management, message.id)

            if(verifyPath("chats",idChat)) {
                val chat = readChat(idChat)
                chat.favoriteMessage(message)
                writeChat(idChat,chat)
            }
        }
    }//UPDATE
    fun unFavoriteMessageUser(message: ChatOperations){
        val management = readLoggedUser().email

        if( verifyPath("users",management) &&  verifyPath("users",message.id) && management !=  ""
                && verifyUserSocialProfile() && message.id != management){
            val idChat = getChatId(management, message.id)


            if(verifyPath("chats",idChat)) {
                val chat = readChat(idChat)
                chat.unFavoriteMessage(message)
                writeChat(idChat,chat)
            }
        }
    }//UPDATE
    fun quoteMessageUser(message: ChatComplexOperations){
        val management = readLoggedUser().email
        val idChat = getChatId(management, message.id)
        if( verifyPath("users",management) &&  verifyPath("users",message.id) && management !=  ""
                && verifyUserSocialProfile() && message.id!= management && verifyPath("chats",idChat)){

            val user = readUser(management)
            val receiver = readUser(message.id)
            val chat = readChat(idChat)
            if(chat.verifyMessage(message.idMessage)) {
                val notification = Inbox("${user.social.getUserName()} sent a new message.", "Message.")
                chat.quoteMessage(message, generateId())
                writeChat(idChat,chat)
                receiver.social.updateInbox(notification)
                writeUser(receiver.getEmail(), receiver)
            }
        }
    }//UPDATE
    fun shareMessageUser(content: ChatComplexOperations){
        val management = readLoggedUser().email

        val idChat = management + content.id

        if( verifyPath("users", management) && management !=  "" &&  verifyPath("users", content.id) && verifyUserSocialProfile()
                && content.id!= management && verifyPath("chats", idChat)){

            val operation = ChatOperations(content.idMessage, content.id)
            val chat = readChat(idChat)
            val message = chat.shareMessage(operation)

            if(message != "" && content.id !in chat.getOwners()){
                val sharedMessage = ChatMessage(message, content.id,true)
                sendMessageUser(sharedMessage)
            }
        }
    } //UPDATE->CREATE
    fun deleteMessageUser(message: ChatOperations){
        val management = readLoggedUser().email
        if (verifyPath("users",management) && verifyPath("users",message.id) && management != ""
                && verifyUserSocialProfile() && message.id != management){

            val user = readUser(management)
            val receiver = readUser(message.id)
            val idChat = getChatId(management, message.id)

            if(verifyPath("chats",idChat)) {
                val chat = readChat(idChat)
                chat.deleteMessage(message)
                writeChat(idChat,chat)
                writeUser(user.getEmail(), user)
                writeUser(receiver.getEmail(), receiver)
            }
        }
    }//DELETE
}