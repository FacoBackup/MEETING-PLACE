package br.meetingplace.management.chat

import br.meetingplace.data.chat.ChatComplexOperations
import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.data.chat.ChatOperations
import br.meetingplace.management.chat.dependencies.ChatGroup
import br.meetingplace.management.chat.dependencies.ChatUser
import br.meetingplace.management.chat.dependencies.Group
import br.meetingplace.management.dependencies.IDs
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteChat
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.services.chat.Chat

class ChatFactory: Group(), ReadWriteChat, ReadWriteCommunity, IDs {
    private val userOp =  ChatUser.getChatUserOperator()
    private val groupOp= ChatGroup.getChatGroupOperator()

    private fun verifyType(id: String?, creator: String?): Int{
        if(!id.isNullOrBlank()){
            val receiverAsUser = readUser(id)
            var receiverAsGroup = readGroup(getGroupId(id,""))

            if(!creator.isNullOrBlank())
                receiverAsGroup = readGroup(getGroupId(id,creator))

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

        when(verifyType(data.idReceiver,data.creator)){
            0->{
               userOp.sendMessage(data)
            }
            1->{
                if(data.idCommunity.isNullOrBlank())
                    groupOp.sendMessage(data)
                else{
                    val community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.groups.checkGroupApproval(data.idReceiver))
                        groupOp.sendMessage(data)
                }

            }
        }

    }
    fun favoriteMessage(data: ChatOperations){
        when(verifyType(data.idReceiver,data.creator)){
            0->{
                userOp.favoriteMessage(data)
            }
            1->{
                if(data.idCommunity.isNullOrBlank())
                    groupOp.favoriteMessage(data)
                else{
                    val community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.groups.checkGroupApproval(data.idReceiver))
                        groupOp.favoriteMessage(data)
                }
            }
        }
    }
    fun unFavoriteMessage(data: ChatOperations){
        when(verifyType(data.idReceiver,data.creator)){
            0->{
                userOp.unFavoriteMessage(data)
            }
            1->{
                if(data.idCommunity.isNullOrBlank())
                    groupOp.unFavoriteMessage(data)
                else{
                    val community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.groups.checkGroupApproval(data.idReceiver))
                        groupOp.unFavoriteMessage(data)
                }
            }
        }
    }

    fun quoteMessage(data: ChatComplexOperations){
        when(verifyType(data.idReceiver,data.creator)){
            0->{
                userOp.quoteMessage(data)
            }
            1->{
                if(data.idCommunity.isNullOrBlank())
                    groupOp.quoteMessage(data)
                else{
                    val community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.groups.checkGroupApproval(data.idReceiver))
                        groupOp.quoteMessage(data)
                }
            }
        }
    }

    fun shareMessage(data: ChatComplexOperations){
        when(verifyType(data.idReceiver,data.creator)){
            0->{
                userOp.shareMessage(data)
            }
            1->{
                if(data.idCommunity.isNullOrBlank())
                    groupOp.shareMessage(data)
                else{
                    val community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.groups.checkGroupApproval(data.idReceiver))
                        groupOp.shareMessage(data)
                }
            }
        }
    }

    fun deleteMessage(data: ChatOperations){
        when(verifyType(data.idReceiver,data.creator)){
            0->{
                userOp.deleteMessage(data)
            }
            1->{
                if(data.idCommunity.isNullOrBlank())
                    groupOp.deleteMessage(data)
                else{
                    val community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.groups.checkGroupApproval(data.idReceiver))
                        groupOp.deleteMessage(data)
                }
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