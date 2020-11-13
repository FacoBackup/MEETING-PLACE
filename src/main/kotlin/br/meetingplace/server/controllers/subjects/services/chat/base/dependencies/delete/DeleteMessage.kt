package br.meetingplace.server.controllers.subjects.services.chat.base.dependencies.delete

import br.meetingplace.server.controllers.dependencies.id.controller.IDController
import br.meetingplace.server.controllers.dependencies.rw.controller.RWController
import br.meetingplace.server.controllers.dependencies.verify.controller.VerifyController
import br.meetingplace.server.dto.chat.ChatSimpleOperator

class DeleteMessage private constructor() : DeleteMessageInterface {
    companion object {
        private val Class = DeleteMessage()
        fun getClass() = Class
    }

    private val iDs = IDController.getClass()
    private val rw = RWController.getClass()
    private val verify = VerifyController.getClass()

    override fun deleteUserMessage(data: ChatSimpleOperator) {
        val user = rw.readUser(data.login.email)
        val receiver = rw.readUser(data.receiver.receiverID)
        val chat = rw.readChat(iDs.getChatId(data.receiver.mainOwnerID, data.receiver.receiverID), data.receiver.mainOwnerID, "", false, community = false)

        if (verify.verifyUser(user) && verify.verifyUser(receiver) && verify.verifyChat(chat)) {
            chat.deleteMessage(data)
            rw.writeChat(chat)
            rw.writeUser(user, user.getEmail())
            rw.writeUser(receiver, receiver.getEmail())
        }
    }//DELETE


    override fun deleteGroupMessage(data: ChatSimpleOperator) {
        when (data.receiver.communityGroup) {
            false -> userDeleteGroupMessage(data)
            true -> communityDeleteGroupMessage(data)
        }
    }

    private fun communityDeleteGroupMessage(data: ChatSimpleOperator) {
        val user = rw.readUser(data.login.email)
        val community = rw.readCommunity(data.receiver.mainOwnerID)
        val group = rw.readGroup(data.receiver.receiverID, data.receiver.mainOwnerID, community = true)
        val chat = rw.readChat("", data.receiver.mainOwnerID, data.receiver.receiverID, group = true, community = true)

        if (verify.verifyUser(user) && verify.verifyGroup(group) && verify.verifyCommunity(community) &&
                community.checkGroupApproval(group.getGroupID()) && chat.verifyMessage(data.messageID)) {

            chat.deleteMessage(data)
            rw.writeChat(chat)
            rw.writeGroup(group)
        }
    }

    private fun userDeleteGroupMessage(data: ChatSimpleOperator) {
        val user = rw.readUser(data.login.email)
        val group = rw.readGroup(data.receiver.receiverID, data.receiver.mainOwnerID, community = false)
        val chat = rw.readChat("", data.receiver.mainOwnerID, data.receiver.receiverID, group = true, community = false)

        if (verify.verifyUser(user) && verify.verifyGroup(group) && chat.verifyMessage(data.messageID)) {
            chat.deleteMessage(data)
            rw.writeChat(chat)
            rw.writeGroup(group)
        }
    }
}