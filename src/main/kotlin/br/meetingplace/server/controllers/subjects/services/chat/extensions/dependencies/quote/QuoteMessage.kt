package br.meetingplace.server.controllers.subjects.services.chat.extensions.dependencies.quote

import br.meetingplace.server.controllers.dependencies.id.controller.IDController
import br.meetingplace.server.controllers.dependencies.rw.controller.RWController
import br.meetingplace.server.controllers.dependencies.verify.controller.VerifyController
import br.meetingplace.server.dto.chat.ChatComplexOperator
import br.meetingplace.server.subjects.services.notification.NotificationData

class QuoteMessage private constructor() : QuoteMessageInterface {
    companion object {
        private val Class = QuoteMessage()
        fun getClass() = Class
    }

    private val iDs = IDController.getClass()
    private val rw = RWController.getClass()
    private val verify = VerifyController.getClass()

    override fun userQuoteMessage(data: ChatComplexOperator) {
        val user = rw.readUser(data.login.email)
        val receiver = rw.readUser(data.receiver.receiverID)
        lateinit var notification: NotificationData

        if (verify.verifyUser(user) && verify.verifyUser(receiver)) {
            val chat = rw.readChat(iDs.getChatId(data.receiver.mainOwnerID, data.receiver.receiverID), data.receiver.mainOwnerID, "", false, community = false)
            if (verify.verifyChat(chat)) {
                notification = NotificationData("${user.getUserName()} sent a new message.", "Message.")
                chat.quoteMessage(data, iDs.generateId())
                rw.writeChat(chat)
                receiver.updateInbox(notification)
                rw.writeUser(receiver, receiver.getEmail())
            }

        }
    }

    override fun groupQuoteMessage(data: ChatComplexOperator) {
        when (data.receiver.communityGroup) {
            false -> {
                val group = rw.readGroup(data.receiver.receiverID, data.receiver.mainOwnerID, false)
                val user = rw.readUser(data.login.email)

                if (verify.verifyUser(user) && verify.verifyGroup(group) && group.verifyMember(user.getEmail())) {
                    val chat = rw.readChat("", user.getEmail(), group.getGroupID(), group = true, false)
                    chat.quoteMessage(data, iDs.generateId())
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
                    chat.quoteMessage(data, iDs.generateId())
                    rw.writeChat(chat)
                }
            }
        }
    }
}