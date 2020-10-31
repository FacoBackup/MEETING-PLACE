package br.meetingplace.management.services.chat.controller

import br.meetingplace.data.Data
import br.meetingplace.data.chat.ChatComplexOperations
import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.data.chat.ChatOperations
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.verify.dependencies.Verify
import br.meetingplace.management.dependencies.readwrite.dependencies.chat.ReadWriteChat
import br.meetingplace.management.dependencies.readwrite.dependencies.community.ReadWriteCommunity
import br.meetingplace.management.dependencies.readwrite.dependencies.group.ReadWriteGroup
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteUser
import br.meetingplace.management.services.chat.dependencies.BaseChatInterface
import br.meetingplace.management.services.chat.dependencies.ChatFeaturesInterface
import br.meetingplace.management.services.chat.dependencies.group.GroupChat
import br.meetingplace.management.services.chat.dependencies.group.GroupChatFeatures
import br.meetingplace.management.services.chat.dependencies.reader.ChatReader
import br.meetingplace.management.services.chat.dependencies.reader.ChatReaderInterface
import br.meetingplace.management.services.chat.dependencies.user.ChatUser
import br.meetingplace.management.services.chat.dependencies.user.UserChatFeatures
import br.meetingplace.services.chat.Chat
import br.meetingplace.services.entitie.User

class ChatController private constructor(): BaseChatInterface, ChatFeaturesInterface, ReadWriteChat, ReadWriteGroup, ReadWriteUser, ReadWriteCommunity, ChatReaderInterface, Verify {
    private val iDs = IDsController.getClass()

    private val userBaseChat =  ChatUser.getClass()
    private val userChatFeatures=  UserChatFeatures.getClass()
    private val groupBaseChat= GroupChat.getClass()
    private val groupChatFeatures = GroupChatFeatures.getClass()

    private val reader = ChatReader.getClass()

    companion object{
        private val Class = ChatController()
        fun getClass()= Class
    }

    override fun sendMessage(data: ChatMessage){
        val user = readUser(readLoggedUser().email)

        if(verifyLoggedUser(user)) {
            when (verifyType(data.idReceiver, user)){
                ChatType.USER_CHAT->{
                   userBaseChat.sendMessage(data)
                }
                ChatType.GROUP_CHAT-> {
                    if (data.idCommunity.isNullOrBlank())
                        groupBaseChat.sendMessage(data)
                    else {
                        val community = readCommunity(iDs.getCommunityId(data.idCommunity))
                        if (verifyCommunity(community) && community.checkGroupApproval(data.idReceiver))
                            groupBaseChat.sendMessage(data)
                    }
                }
            }
        }

    }
    override fun favoriteMessage(data: ChatOperations){
        val user = readUser(readLoggedUser().email)

        if(verifyLoggedUser(user)) {
            when (verifyType(data.idReceiver, user)) {
                ChatType.USER_CHAT -> {
                    userChatFeatures.favoriteMessage(data)
                }
                ChatType.GROUP_CHAT -> {
                    if (data.idCommunity.isNullOrBlank())
                        groupChatFeatures.favoriteMessage(data)
                    else {
                        val community = readCommunity(iDs.getCommunityId(data.idCommunity))
                        if (verifyCommunity(community) && community.checkGroupApproval(data.idReceiver))
                            groupChatFeatures.favoriteMessage(data)
                    }
                }
            }
        }
    }
    override fun unFavoriteMessage(data: ChatOperations){
        val user = readUser(readLoggedUser().email)

        if(verifyLoggedUser(user)) {
            when (verifyType(data.idReceiver, user)) {
                ChatType.USER_CHAT -> {
                    userChatFeatures.unFavoriteMessage(data)
                }
                ChatType.GROUP_CHAT -> {
                    if (data.idCommunity.isNullOrBlank())
                        groupChatFeatures.unFavoriteMessage(data)
                    else {
                        val community = readCommunity(iDs.getCommunityId(data.idCommunity))
                        if (verifyCommunity(community) && community.checkGroupApproval(data.idReceiver))
                            groupChatFeatures.unFavoriteMessage(data)
                    }
                }
            }
        }
    }

    override fun quoteMessage(data: ChatComplexOperations){
        val user = readUser(readLoggedUser().email)

        if(verifyLoggedUser(user)) {
            when (verifyType(data.idReceiver, user)) {
                ChatType.USER_CHAT -> {
                    userChatFeatures.quoteMessage(data)
                }
                ChatType.GROUP_CHAT -> {
                    if (data.idCommunity.isNullOrBlank())
                        groupChatFeatures.quoteMessage(data)
                    else {
                        val community = readCommunity(iDs.getCommunityId(data.idCommunity))
                        if (verifyCommunity(community) && community.checkGroupApproval(data.idReceiver))
                            groupChatFeatures.quoteMessage(data)
                    }
                }
            }
        }
    }

    override fun shareMessage(data: ChatComplexOperations){
        val user = readUser(readLoggedUser().email)

        if(verifyLoggedUser(user)) {
            when (verifyType(data.idReceiver, user)){
                ChatType.USER_CHAT -> {
                    userChatFeatures.shareMessage(data)
                }
                ChatType.GROUP_CHAT -> {
                    if (data.idCommunity.isNullOrBlank())
                        groupChatFeatures.shareMessage(data)
                    else {
                        val community = readCommunity(iDs.getCommunityId(data.idCommunity))
                        if (verifyCommunity(community) && community.checkGroupApproval(data.idReceiver))
                            groupChatFeatures.shareMessage(data)
                    }
                }
            }
        }
    }

    override fun deleteMessage(data: ChatOperations){
        val user = readUser(readLoggedUser().email)

        if(verifyLoggedUser(user)){
            when(verifyType(data.idReceiver,user)){
                ChatType.USER_CHAT->{
                    userBaseChat.deleteMessage(data)
                }
                ChatType.GROUP_CHAT->{
                    if(data.idCommunity.isNullOrBlank())
                        groupBaseChat.deleteMessage(data)
                    else{
                        val community = readCommunity(iDs.getCommunityId(data.idCommunity))
                        if(verifyCommunity(community) && community.checkGroupApproval(data.idReceiver))
                            groupBaseChat.deleteMessage(data)
                    }
                }
            }
        }
    }

    override fun seeChat(data: Data): Chat?{
        return reader.seeChat(data)
    }

    private fun verifyType(id: String?, loggedUser: User): ChatType?{
        if(!id.isNullOrBlank()){
            val receiverAsUser = readUser(id)
            val receiverAsGroup = readGroup(iDs.simpleToStandardIdGroup(id, loggedUser))

            return if(!verifyGroup(receiverAsGroup) && verifyUser(receiverAsUser))
                ChatType.USER_CHAT
            else if(verifyGroup(receiverAsGroup) && !verifyUser(receiverAsUser))
                ChatType.GROUP_CHAT
            else
                null
        }
        return null
    }
}