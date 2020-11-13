package br.meetingplace.server.controllers.subjects.services.chat.base.delete

import br.meetingplace.server.dto.chat.ChatSimpleOperator

class DeleteMessage private constructor() {
    companion object {
        private val Class = DeleteMessage()
        fun getClass() = Class
    }
    private fun getChatID(creator: String, external: String): String{

    }

    fun deleteUserMessage(data: ChatSimpleOperator) {
        val user = rw.readUser(data.login.email)
        val receiver = rw.readUser(data.receiver.receiverID)
        val chat = rw.readChat(getChatID(data.receiver.mainOwnerID, data.receiver.receiverID), data.receiver.mainOwnerID, "", false, community = false)

        if (user != null && receiver != null  && chat != null) {
            chat.deleteMessage(data)
            rw.writeChat(chat)
            rw.writeUser(user, user.getEmail())
            rw.writeUser(receiver, receiver.getEmail())
        }
    }//DELETE


    fun deleteGroupMessage(data: ChatSimpleOperator) {
        when (data.receiver.communityGroup) {
            false -> userDeleteGroupMessage(data)
            true -> communityDeleteGroupMessage(data)
        }
    }

    private fun communityDeleteGroupMessage(data: ChatSimpleOperator) {
        val user = rw.readUser(data.login.email)
        val community = rw.readCommunity(data.receiver.mainOwnerID)
        val group = rw.readGroup(data.receiver.receiverID, data.receiver.mainOwnerID, community = true)
        val chat = rw.readChat(group.getChatID())

        if (user != null && group != null  && community != null &&
                community.checkGroupApproval(group.getGroupID()) && chat.verifyMessage(data.messageID)) {

            chat.deleteMessage(data)
            rw.writeChat(chat)
            rw.writeGroup(group)
        }
    }

    private fun userDeleteGroupMessage(data: ChatSimpleOperator) {
        val user = rw.readUser(data.login.email)
        val group = rw.readGroup(data.receiver.receiverID, data.receiver.mainOwnerID, community = false)
        val chat = rw.readChat(group.getChatID())

        if (user != null && group != null && chat.verifyMessage(data.messageID)) {
            chat.deleteMessage(data)
            rw.writeChat(chat)
            rw.writeGroup(group)
        }
    }
}