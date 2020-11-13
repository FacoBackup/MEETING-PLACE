package br.meetingplace.server.controllers.subjects.services.chat.base.controller

import br.meetingplace.server.controllers.subjects.services.chat.dependencies.base.dependencies.delete.DeleteMessage
import br.meetingplace.server.controllers.subjects.services.chat.dependencies.base.dependencies.send.SendMessage
import br.meetingplace.server.dto.chat.ChatSimpleOperator
import br.meetingplace.server.dto.chat.MessageData

class BaseChatController private constructor() : _root_ide_package_.br.meetingplace.server.controllers.subjects.services.chat.base.controller.BaseChatControllerInterface {
    companion object {
        private val Class = _root_ide_package_.br.meetingplace.server.controllers.subjects.services.chat.base.controller.BaseChatController()
        fun getClass() = _root_ide_package_.br.meetingplace.server.controllers.subjects.services.chat.base.controller.BaseChatController.Companion.Class
    }

    private val send = SendMessage.getClass()
    private val delete = DeleteMessage.getClass()

    override fun deleteMessage(data: ChatSimpleOperator) {

        when (data.receiver.userGroup || data.receiver.communityGroup) {
            true -> delete.deleteGroupMessage(data)
            false -> delete.deleteUserMessage(data)
        }
    }

    override fun sendMessage(data: MessageData) {
        when (data.receiver.userGroup || data.receiver.communityGroup) {
            true -> send.sendGroupMessage(data)
            false -> send.sendUserMessage(data)
        }
    }
}