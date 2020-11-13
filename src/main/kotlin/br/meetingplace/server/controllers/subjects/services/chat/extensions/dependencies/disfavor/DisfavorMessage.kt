package br.meetingplace.server.controllers.subjects.services.chat.extensions.dependencies.disfavor

import br.meetingplace.server.controllers.dependencies.id.controller.IDController
import br.meetingplace.server.controllers.dependencies.rw.controller.RWController
import br.meetingplace.server.controllers.dependencies.verify.controller.VerifyController
import br.meetingplace.server.dto.chat.ChatSimpleOperator

class DisfavorMessage private constructor() : DisfavorMessageInterface {
    companion object {
        private val Class = DisfavorMessage()
        fun getClass() = Class
    }

    private val iDs = IDController.getClass()
    private val rw = RWController.getClass()
    private val verify = VerifyController.getClass()

    override fun userDisfavorMessage(data: ChatSimpleOperator) {
        val user = rw.readUser(data.login.email)
        val receiver = rw.readUser(data.receiver.receiverID)
        val chat = rw.readChat(iDs.getChatId(data.receiver.mainOwnerID, data.receiver.receiverID), data.receiver.mainOwnerID, "", group = false, community = false)

        if (verify.verifyUser(user) && verify.verifyUser(receiver) && verify.verifyChat(chat)) {
            chat.disfavorMessage(data)
            rw.writeChat(chat)
        }
    }

    override fun groupDisfavorMessage(data: ChatSimpleOperator) {
        when (data.receiver.communityGroup) {
            false -> {
                val group = rw.readGroup(data.receiver.receiverID, data.receiver.mainOwnerID, false)
                val user = rw.readUser(data.login.email)

                if (verify.verifyUser(user) && verify.verifyGroup(group) && group.verifyMember(user.getEmail())) {
                    val chat = rw.readChat("", user.getEmail(), group.getGroupID(), group = true, false)
                    chat.disfavorMessage(data)
                    rw.writeChat(chat)
                }
            }
            true -> {
                val group = rw.readGroup(data.receiver.receiverID, data.receiver.mainOwnerID, true)
                val user = rw.readUser(data.login.email)
                val community = rw.readCommunity(data.receiver.mainOwnerID)

                if (verify.verifyUser(user) && verify.verifyGroup(group) && verify.verifyCommunity(community)
                        && community.checkGroupApproval(group.getGroupID()) && group.verifyMember(user.getEmail())) {
                    val chat = rw.readChat("", community.getID(), group.getGroupID(), group = true, true)
                    chat.disfavorMessage(data)
                    rw.writeChat(chat)
                }
            }
        }
    }
}