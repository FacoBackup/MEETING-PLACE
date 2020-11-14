package br.meetingplace.server.controllers.subjects.services.chat.base.delete

import br.meetingplace.server.controllers.readwrite.chat.ChatRWInterface
import br.meetingplace.server.controllers.readwrite.community.CommunityRWInterface
import br.meetingplace.server.controllers.readwrite.group.GroupRWInterface
import br.meetingplace.server.controllers.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.chat.ChatSimpleOperator

class DeleteMessage private constructor() {
    companion object {
        private val Class = DeleteMessage()
        fun getClass() = Class
    }

    fun deleteMessage(data: ChatSimpleOperator, rwUser: UserRWInterface, rwGroup: GroupRWInterface, rwCommunity: CommunityRWInterface, rwChat: ChatRWInterface){
        val user = rwUser.read(data.login.email)
        if(user != null){
            when(data.receiver.userGroup || data.receiver.communityGroup){
                true->{ //GROUP
                    val group = rwGroup.read(data.receiver.receiverID)
                    if(group != null){
                        val chat = rwChat.read(group.getChatID())
                        when(data.receiver.communityGroup){
                            true->{
                                val community = rwCommunity.read(group.getOwner().groupOwnerID)
                                if(community != null && chat != null) {
                                    chat.deleteMessage(data)
                                    rwChat.write(chat)
                                }

                            }
                            false-> if(chat!= null){
                                        chat.deleteMessage(data)
                                        rwChat.write(chat)
                                    }
                        }
                    }
                }
                false->{ //USER <-> USER
                    val chat = rwChat.read(data.receiver.chatID)
                    if(chat!= null){
                        chat.deleteMessage(data)
                        rwChat.write(chat)
                    }
                }
            }
        }
    }
//
//    fun deleteUserMessage(data: ChatSimpleOperator) {
//        val user = rw.readUser(data.login.email)
//        val receiver = rw.readUser(data.receiver.receiverID)
//        val chat = rw.readChat(getChatID(data.receiver.mainOwnerID, data.receiver.receiverID), data.receiver.mainOwnerID, "", false, community = false)
//
//        if (user != null && receiver != null  && chat != null) {
//            chat.deleteMessage(data)
//            rw.writeChat(chat)
//            rw.writeUser(user, user.getEmail())
//            rw.writeUser(receiver, receiver.getEmail())
//        }
//    }//DELETE
//
//
//    fun deleteGroupMessage(data: ChatSimpleOperator) {
//        when (data.receiver.communityGroup) {
//            false -> userDeleteGroupMessage(data)
//            true -> communityDeleteGroupMessage(data)
//        }
//    }
//
//    private fun communityDeleteGroupMessage(data: ChatSimpleOperator) {
//        val user = rw.readUser(data.login.email)
//        val community = rw.readCommunity(data.receiver.mainOwnerID)
//        val group = rw.readGroup(data.receiver.receiverID, data.receiver.mainOwnerID, community = true)
//        val chat = rw.readChat(group.getChatID())
//
//        if (user != null && group != null  && community != null &&
//                community.checkGroupApproval(group.getGroupID()) && chat.verifyMessage(data.messageID)) {
//
//            chat.deleteMessage(data)
//            rw.writeChat(chat)
//            rw.writeGroup(group)
//        }
//    }
//
//    private fun userDeleteGroupMessage(data: ChatSimpleOperator) {
//        val user = rw.readUser(data.login.email)
//        val group = rw.readGroup(data.receiver.receiverID, data.receiver.mainOwnerID, community = false)
//        val chat = rw.readChat(group.getChatID())
//
//        if (user != null && group != null && chat.verifyMessage(data.messageID)) {
//            chat.deleteMessage(data)
//            rw.writeChat(chat)
//            rw.writeGroup(group)
//        }
//    }
}