package br.meetingplace.management.core.chat

import br.meetingplace.data.chat.ChatComplexOperations
import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.data.chat.ChatOperations
import br.meetingplace.management.core.chat.dependencies.ChatGroup
import br.meetingplace.management.core.chat.dependencies.ChatUser
import br.meetingplace.management.core.chat.dependencies.Group
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteChat
import br.meetingplace.services.chat.Chat

class ChatOperator: Group(), ReadWriteChat{
    private val userOp =  ChatUser.getChatUserOperator()
    private val groupOp= ChatGroup.getChatGroupOperator()

    private fun verifyType(id: String?): Int{
        if(!id.isNullOrBlank()){
            val receiverAsUser = readUser(id)
            val receiverAsGroup = readGroup(id)

            return if(receiverAsGroup.getGroupId() == "" && receiverAsUser.getEmail() != "") // is a user
                0
            else if(receiverAsGroup.getGroupId() != "" && receiverAsUser.getEmail() == "") // is a group
                1
            else // is nothing
                -1
        }
        return -1
    }
    fun sendMessage(data: ChatMessage){
        when(verifyType(data.idReceiver)){
            0->{
               userOp.sendMessage(data)
            }
            1->{
                groupOp.sendMessage(data)
            }
        }

    }
    fun favoriteMessage(data: ChatOperations){
        when(verifyType(data.idReceiver)){
            0->{
                userOp.favoriteMessage(data)
            }
            1->{
                groupOp.favoriteMessage(data)
            }
        }
    }
    fun unFavoriteMessage(data: ChatOperations){
        when(verifyType(data.idReceiver)){
            0->{
                userOp.unFavoriteMessage(data)
            }
            1->{
                groupOp.unFavoriteMessage(data)
            }
        }
    }
    fun quoteMessage(data: ChatComplexOperations){
        when(verifyType(data.idReceiver)){
            0->{
                userOp.quoteMessage(data)
            }
            1->{
                groupOp.quoteMessage(data)
            }
        }
    }
    fun shareMessage(data: ChatComplexOperations){
        when(verifyType(data.idReceiver)){
            0->{
                userOp.shareMessage(data)
            }
            1->{
                groupOp.shareMessage(data)
            }
        }
    }
    fun deleteMessage(data: ChatOperations){
        when(verifyType(data.idReceiver)){
            0->{
                userOp.deleteMessage(data)
            }
            1->{
                groupOp.deleteMessage(data)
            }
        }
    }

    fun getMyChats(): List<Chat> {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val chats = mutableListOf<Chat>()

        if(verifyUser(user)){
            val userChats = user.social.getMyChats()
            for(i in 0 until userChats.size){
                val chat = readChat(userChats[i])
                if (verifyChat(chat))
                    chats.add(chat)
            }
            return chats
        }
        return chats
    }


}