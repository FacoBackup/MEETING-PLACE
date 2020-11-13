package br.meetingplace.server.controllers.subjects.services.chat.base.send

import br.meetingplace.server.controllers.subjects.services.chat.factory.ChatFactory
import br.meetingplace.server.dto.chat.MessageData
import br.meetingplace.server.subjects.entities.User
import br.meetingplace.server.subjects.services.chat.Chat
import br.meetingplace.server.subjects.services.chat.SimplifiedChat
import br.meetingplace.server.subjects.services.chat.dependencies.data.MessageContent
import br.meetingplace.server.subjects.services.chat.dependencies.data.MessageType
import br.meetingplace.server.subjects.services.groups.Group
import br.meetingplace.server.subjects.services.notification.NotificationData

class SendMessage private constructor() {
    companion object {
        private val Class = SendMessage()
        fun getClass() = Class
    }

    fun sendUserMessage(data: MessageData) {
        val user = rw.readUser(data.login.email)
        val receiver = rw.readUser(data.receiver.receiverID)
        lateinit var simplifiedChat: SimplifiedChat
        lateinit var msg: MessageContent
        lateinit var notification: NotificationData

        if (verify.verifyUser(user) && verify.verifyUser(receiver)) {
            val chat = rw.readChat(iDs.getChatId(data.receiver.mainOwnerID, data.receiver.receiverID), data.receiver.mainOwnerID, "", group = false, community = false)
            when (verify.verifyChat(chat)) {
                true -> { //The chat exists
                    msg = MessageContent(data.message, iDs.generateId(), data.login.email, data.type
                            ?: MessageType.NORMAL)
                    notification = NotificationData("${user.getUserName()} sent a new message.", "Message.")
                    chat.addMessage(msg)
                    rw.writeChat(chat)

                    simplifiedChat = SimplifiedChat(chat.getOwner(), chat.getID())
                    receiver.updateMyChats(simplifiedChat)
                    receiver.updateInbox(notification)
                    user.updateMyChats(simplifiedChat)

                    rw.writeUser(user, user.getEmail())
                    rw.writeUser(receiver, receiver.getEmail())

                }
                false -> ChatFactory.getClass().createChat(data) //The chat doesn't exist
            }
        }
    }

    fun sendGroupMessage(data: MessageData) {
        when (data.receiver.communityGroup) {
            false -> {
                val group = rw.readGroup(data.receiver.receiverID, data.receiver.mainOwnerID, community = false)
                val user = rw.readUser(data.login.email)
                if (verify.verifyUser(user) && verify.verifyGroup(group)) {
                    println("step 1")
                    sendMessage(data.message,
                            user,
                            data.login.email,
                            group,
                            rw.readChat("", data.receiver.mainOwnerID, group.getGroupID(), true, community = false))
                }

            }
            true -> {
                val community = rw.readCommunity(data.receiver.mainOwnerID)
                val group = rw.readGroup(data.receiver.receiverID, data.receiver.mainOwnerID, community = true)
                val user = rw.readUser(data.login.email)

                if (verify.verifyUser(user) && verify.verifyCommunity(community) && verify.verifyGroup(group) && community.checkGroupApproval(group.getGroupID())) {

                    sendMessage(data.message,
                            user,
                            data.login.email,
                            group,
                            rw.readChat("", community.getID(), group.getGroupID(), group = true, community = true))
                }

            }
        }
    }

    private fun sendMessage(message: String, user: User, logged: String, group: Group, chat: Chat) {
        println("step 2")
        val messageContent = MessageContent(message, iDs.generateId(), logged, MessageType.NORMAL)
        val notification = NotificationData("${user.getUserName()} from ${group.getGroupID()} sent a new message.", Type = "Group Message.")
        val groupMembers = group.getMembers()
        chat.addMessage(messageContent)
        rw.writeChat(chat)
        println("done")
        for (i in groupMembers.indices) {
            val member = rw.readUser(groupMembers[i])
            if (verify.verifyUser(member) && groupMembers[i] != logged) {
                member.updateInbox(notification)
                rw.writeUser(member, member.getEmail())
            }
        }

        val creator = rw.readUser(group.getCreator())
        if (logged != group.getCreator() && verify.verifyUser(creator)) {
            creator.updateInbox(notification)
            rw.writeUser(creator, creator.getEmail())
        }

    }
}