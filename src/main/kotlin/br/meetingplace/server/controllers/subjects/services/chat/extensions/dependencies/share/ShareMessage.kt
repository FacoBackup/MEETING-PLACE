package br.meetingplace.server.controllers.subjects.services.chat.extensions.dependencies.share

import br.meetingplace.server.controllers.dependencies.id.controller.IDController
import br.meetingplace.server.controllers.dependencies.rw.controller.RWController
import br.meetingplace.server.controllers.dependencies.verify.controller.VerifyController
import br.meetingplace.server.controllers.subjects.services.chat.base.controller.BaseChatController
import br.meetingplace.server.dto.chat.ChatComplexOperator
import br.meetingplace.server.dto.chat.MessageData
import br.meetingplace.server.subjects.services.chat.dependencies.data.MessageType

class ShareMessage private constructor() : ShareMessageInterface {
    companion object {
        private val Class = ShareMessage()
        fun getClass() = Class
    }

    private val iDs = IDController.getClass()
    private val rw = RWController.getClass()
    private val verify = VerifyController.getClass()

    override fun userShareMessage(data: ChatComplexOperator) {
        val user = rw.readUser(data.login.email)
        val receiver = rw.readUser(data.receiver.receiverID)

        if (verify.verifyUser(user) && verify.verifyUser(receiver)) {
            val chat = rw.readChat(iDs.getChatId(data.source.mainOwnerID, data.source.receiverID), data.source.mainOwnerID, "", false, community = false)
            if (verify.verifyChat(chat)) {
                val message = chat.shareMessage(data)
                if (!message.isNullOrBlank())
                    _root_ide_package_.br.meetingplace.server.controllers.subjects.services.chat.base.controller.BaseChatController.getClass().sendMessage(MessageData("|Shared| ${data.message}", MessageType.SHARED, data.receiver, data.login))
            }
        }
    }

    override fun groupShareMessage(data: ChatComplexOperator) {
        when (data.receiver.communityGroup) {
            false -> {
                val group = rw.readGroup(data.source.receiverID, data.source.mainOwnerID, false)
                val user = rw.readUser(data.login.email)

                if (verify.verifyUser(user) && verify.verifyGroup(group) && group.verifyMember(user.getEmail())) {
                    val chat = rw.readChat("", user.getEmail(), group.getGroupID(), group = true, false)
                    val messageContent = chat.shareMessage(data)

                    if (!messageContent.isNullOrBlank())
                        _root_ide_package_.br.meetingplace.server.controllers.subjects.services.chat.base.controller.BaseChatController.getClass().sendMessage(MessageData("|Shared| $messageContent", MessageType.SHARED, data.receiver, data.login))
                }
            }
            true -> {
                val group = rw.readGroup(data.source.receiverID, data.source.mainOwnerID, true)
                val user = rw.readUser(data.login.email)
                val community = rw.readCommunity(data.source.mainOwnerID)

                if (verify.verifyUser(user) && verify.verifyGroup(group) && verify.verifyCommunity(community)
                        && community.checkGroupApproval(group.getGroupID()) && group.verifyMember(user.getEmail())) {
                    val chat = rw.readChat("", community.getID(), group.getGroupID(), group = true, true)
                    val messageContent = chat.shareMessage(data)

                    if (!messageContent.isNullOrBlank())
                        _root_ide_package_.br.meetingplace.server.controllers.subjects.services.chat.base.controller.BaseChatController.getClass().sendMessage(MessageData("|Shared| $messageContent", MessageType.SHARED, data.receiver, data.login))
                }
            }
        }
    }
}