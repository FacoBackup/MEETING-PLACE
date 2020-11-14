package br.meetingplace.server.controllers.subjects.services.chat.extensions.share

import br.meetingplace.server.controllers.readwrite.chat.ChatRWInterface
import br.meetingplace.server.controllers.readwrite.community.CommunityRWInterface
import br.meetingplace.server.controllers.readwrite.group.GroupRWInterface
import br.meetingplace.server.controllers.readwrite.user.UserRWInterface
import br.meetingplace.server.controllers.subjects.services.chat.base.send.SendMessage
import br.meetingplace.server.dto.chat.ChatComplexOperator
import br.meetingplace.server.dto.chat.MessageData
import br.meetingplace.server.subjects.services.chat.dependencies.data.MessageType

class ShareMessage private constructor() {
    companion object {
        private val Class = ShareMessage()
        fun getClass() = Class
    }

    fun shareMessage(data: ChatComplexOperator, rwUser: UserRWInterface, rwGroup: GroupRWInterface, rwCommunity: CommunityRWInterface, rwChat: ChatRWInterface){
        val user = rwUser.read(data.login.email)
        if(user != null){
            when(data.source.userGroup || data.source.communityGroup){
                true -> { //GROUP
                    val group = rwGroup.read(data.source.receiverID)
                    if (group != null) {
                        val chat = rwChat.read(group.getChatID())
                        when (data.receiver.communityGroup) {
                            true -> {
                                val community = rwCommunity.read(group.getOwner().groupOwnerID)
                                if (community != null && chat != null) {
                                    val messageContent = chat.shareMessage(data)
                                    SendMessage.getClass().sendMessage(MessageData("|Shared| $messageContent", MessageType.SHARED, data.receiver, data.login), rwUser, rwGroup, rwCommunity, rwChat)
                                    rwChat.write(chat)
                                }
                            }
                            false -> {
                                if(chat != null){
                                    val messageContent = chat.shareMessage(data)
                                    SendMessage.getClass().sendMessage(MessageData("|Shared| $messageContent", MessageType.SHARED, data.receiver, data.login), rwUser, rwGroup, rwCommunity, rwChat)
                                    rwChat.write(chat)
                                }
                            }
                        }
                    }
                }
                false->{ //USER <-> USER
                    val chat = rwChat.read(data.source.chatID)
                    if(chat != null){
                        val messageContent = chat.shareMessage(data)
                        SendMessage.getClass().sendMessage(MessageData("|Shared| $messageContent", MessageType.SHARED, data.receiver, data.login), rwUser, rwGroup, rwCommunity, rwChat)
                        rwChat.write(chat)
                    }
                }
            }
        }
    }

//
//    fun userShareMessage(data: ChatComplexOperator) {
//        val user = rw.readUser(data.login.email)
//        val receiver = rw.readUser(data.receiver.receiverID)
//
//        if (verify.verifyUser(user) && verify.verifyUser(receiver)) {
//            val chat = rw.readChat(iDs.getChatId(data.source.ownerID, data.source.receiverID), data.source.ownerID, "", false, community = false)
//            if (verify.verifyChat(chat)) {
//                val message = chat.shareMessage(data)
//                if (!message.isNullOrBlank())
//                    _root_ide_package_.br.meetingplace.server.controllers.subjects.services.chat.base.controller.BaseChatController.getClass().sendMessage(MessageData("|Shared| ${data.message}", MessageType.SHARED, data.receiver, data.login))
//            }
//        }
//    }
//
//    fun groupShareMessage(data: ChatComplexOperator) {
//        when (data.receiver.communityGroup) {
//            false -> {
//                val group = rw.readGroup(data.source.receiverID, data.source.ownerID, false)
//                val user = rw.readUser(data.login.email)
//
//                if (verify.verifyUser(user) && verify.verifyGroup(group) && group.verifyMember(user.getEmail())) {
//                    val chat = rw.readChat("", user.getEmail(), group.getGroupID(), group = true, false)
//                    val messageContent = chat.shareMessage(data)
//
//                    if (!messageContent.isNullOrBlank())
//                        _root_ide_package_.br.meetingplace.server.controllers.subjects.services.chat.base.controller.BaseChatController.getClass().sendMessage(MessageData("|Shared| $messageContent", MessageType.SHARED, data.receiver, data.login))
//                }
//            }
//            true -> {
//                val group = rw.readGroup(data.source.receiverID, data.source.ownerID, true)
//                val user = rw.readUser(data.login.email)
//                val community = rw.readCommunity(data.source.ownerID)
//
//                if (verify.verifyUser(user) && verify.verifyGroup(group) && verify.verifyCommunity(community)
//                        && community.checkGroupApproval(group.getGroupID()) && group.verifyMember(user.getEmail())) {
//                    val chat = rw.readChat("", community.getID(), group.getGroupID(), group = true, true)
//                    val messageContent = chat.shareMessage(data)
//
//                    if (!messageContent.isNullOrBlank())
//                        _root_ide_package_.br.meetingplace.server.controllers.subjects.services.chat.base.controller.BaseChatController.getClass().sendMessage(MessageData("|Shared| $messageContent", MessageType.SHARED, data.receiver, data.login))
//                }
//            }
//        }
//    }
}