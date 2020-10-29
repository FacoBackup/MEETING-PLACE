package br.meetingplace.management.services.chat.core

import br.meetingplace.data.Data
import br.meetingplace.data.chat.ChatComplexOperations
import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.data.chat.ChatOperations
import br.meetingplace.management.dependencies.IDs
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteChat
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.management.services.chat.dependencies.BaseChatInterface
import br.meetingplace.management.services.chat.dependencies.ChatFeaturesInterface
import br.meetingplace.management.services.chat.dependencies.group.GroupChat
import br.meetingplace.management.services.chat.dependencies.group.GroupFeatures
import br.meetingplace.management.services.chat.dependencies.reader.ChatReaderInterface
import br.meetingplace.management.services.group.Group
import br.meetingplace.management.services.chat.dependencies.user.ChatUser
import br.meetingplace.management.services.chat.dependencies.user.UserChatFeatures
import br.meetingplace.services.chat.Chat

class ChatCore private constructor(): BaseChatInterface, ChatFeaturesInterface, Group(), ReadWriteChat, ReadWriteCommunity, IDs, ChatReaderInterface {

    private val userBaseChat =  ChatUser.getClass()
    private val userChatFeatures=  UserChatFeatures.getClass()

    private val groupBaseChat= GroupChat.getClass()
    private val groupChatFeatures = GroupFeatures.getClass()

    companion object{
        private val Class = ChatCore()
        fun getCore()= Class
    }

    private fun verifyType(id: String?, creator: String?): Int{
        if(!id.isNullOrBlank()){
            val receiverAsUser = readUser(id)
            val receiverAsGroup = readGroup(getGroupId(id,if(!creator.isNullOrBlank()) creator else ""))

            return if(receiverAsGroup.getGroupId() == "" && receiverAsUser.getEmail() != "") // is a user
                0
            else if(receiverAsGroup.getGroupId() != "" && receiverAsUser.getEmail() == "") // is a group
                1
            else // is nothing
                -1
        }
        return -1
    }

    override fun sendMessage(data: ChatMessage){

        when(verifyType(data.idReceiver,data.creator)){
            0->{
               userBaseChat.sendMessage(data)
            }
            1->{
                if(data.idCommunity.isNullOrBlank())
                    groupBaseChat.sendMessage(data)
                else{
                    val community = readCommunity(getCommunityId(data.idCommunity))
                    if(verifyCommunity(community) && community.groups.checkGroupApproval(data.idReceiver))
                        groupBaseChat.sendMessage(data)
                }

            }
        }

    }
    override fun favoriteMessage(data: ChatOperations){
        when(verifyType(data.idReceiver,data.creator)){
            0->{
                userChatFeatures.favoriteMessage(data)
            }
            1->{
                if(data.idCommunity.isNullOrBlank())
                    groupChatFeatures.favoriteMessage(data)
                else{
                    val community = readCommunity(getCommunityId(data.idCommunity))
                    if(verifyCommunity(community) && community.groups.checkGroupApproval(data.idReceiver))
                        groupChatFeatures.favoriteMessage(data)
                }
            }
        }
    }
    override fun unFavoriteMessage(data: ChatOperations){
        when(verifyType(data.idReceiver,data.creator)){
            0->{
                userChatFeatures.unFavoriteMessage(data)
            }
            1->{
                if(data.idCommunity.isNullOrBlank())
                    groupChatFeatures.unFavoriteMessage(data)
                else{
                    val community = readCommunity(getCommunityId(data.idCommunity))
                    if(verifyCommunity(community) && community.groups.checkGroupApproval(data.idReceiver))
                        groupChatFeatures.unFavoriteMessage(data)
                }
            }
        }
    }

    override fun quoteMessage(data: ChatComplexOperations){
        when(verifyType(data.idReceiver,data.creator)){
            0->{
                userChatFeatures.quoteMessage(data)
            }
            1->{
                if(data.idCommunity.isNullOrBlank())
                    groupChatFeatures.quoteMessage(data)
                else{
                    val community = readCommunity(getCommunityId(data.idCommunity))
                    if(verifyCommunity(community) && community.groups.checkGroupApproval(data.idReceiver))
                        groupChatFeatures.quoteMessage(data)
                }
            }
        }
    }

    override fun shareMessage(data: ChatComplexOperations){
        when(verifyType(data.idReceiver,data.creator)){
            0->{
                userChatFeatures.shareMessage(data)
            }
            1->{
                if(data.idCommunity.isNullOrBlank())
                    groupChatFeatures.shareMessage(data)
                else{
                    val community = readCommunity(getCommunityId(data.idCommunity))
                    if(verifyCommunity(community) && community.groups.checkGroupApproval(data.idReceiver))
                        groupChatFeatures.shareMessage(data)
                }
            }
        }
    }

    override fun deleteMessage(data: ChatOperations){
        when(verifyType(data.idReceiver,data.creator)){
            0->{
                userBaseChat.deleteMessage(data)
            }
            1->{
                if(data.idCommunity.isNullOrBlank())
                    groupBaseChat.deleteMessage(data)
                else{
                    val community = readCommunity(getCommunityId(data.idCommunity))
                    if(verifyCommunity(community) && community.groups.checkGroupApproval(data.idReceiver))
                        groupBaseChat.deleteMessage(data)
                }
            }
        }
    }

    override fun seeChat(data: Data) {
        TODO("Not yet implemented")
    }

    override fun seeGroupChat(data: Data) {
        TODO("Not yet implemented")
    }
}