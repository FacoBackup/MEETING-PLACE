package br.meetingplace.server.controllers.subjects.services.chat.factory

import br.meetingplace.server.controllers.dependencies.id.controller.IDController
import br.meetingplace.server.controllers.dependencies.rw.controller.RWController
import br.meetingplace.server.controllers.dependencies.verify.controller.VerifyController
import br.meetingplace.server.dto.chat.MessageData
import br.meetingplace.server.subjects.services.chat.Chat
import br.meetingplace.server.subjects.services.chat.SimplifiedChat
import br.meetingplace.server.subjects.services.chat.dependencies.data.MessageContent
import br.meetingplace.server.subjects.services.chat.dependencies.data.MessageType
import br.meetingplace.server.subjects.services.notification.NotificationData
import br.meetingplace.server.subjects.services.owner.OwnerType
import br.meetingplace.server.subjects.services.owner.chat.ChatOwnerData

class ChatFactory : ChatFactoryInterface {

    private val iDs = IDController.getClass()
    private val rw = RWController.getClass()
    private val verify = VerifyController.getClass()

    companion object {
        private val Class = ChatFactory()
        fun getClass() = Class
    }

    override fun createChat(data: MessageData) {
        val logged = data.login.email
        val user = rw.readUser(data.login.email)
        val receiver = rw.readUser(data.receiver.receiverID)

        lateinit var messageContent: MessageContent
        lateinit var notification: NotificationData
        lateinit var chatID: String
        lateinit var chat: Chat
        lateinit var simplifiedChat: SimplifiedChat

        if (verify.verifyUser(user) && verify.verifyUser(receiver)) {
            chatID = iDs.getChatId(logged, receiver.getEmail())
            chat = Chat(chatID, ChatOwnerData(user.getEmail(), user.getEmail(), OwnerType.USER, OwnerType.USER))
            notification = NotificationData("${user.getUserName()} started a conversation with you.", "Message.")

            messageContent = MessageContent(data.message, iDs.generateId(), logged, data.type ?: MessageType.NORMAL)
            chat.addMessage(messageContent)

            simplifiedChat = SimplifiedChat(chat.getOwner(), data.receiver.receiverID)
            user.updateMyChats(simplifiedChat)
            receiver.updateMyChats(simplifiedChat)
            receiver.updateInbox(notification)

            rw.writeChat(chat)
            rw.writeUser(user, user.getEmail())
            rw.writeUser(receiver, receiver.getEmail())
        }
    }
}