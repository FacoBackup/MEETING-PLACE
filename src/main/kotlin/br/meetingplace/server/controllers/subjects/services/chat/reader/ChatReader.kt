package br.meetingplace.server.controllers.subjects.services.chat.reader

import br.meetingplace.server.controllers.dependencies.readwrite.chat.ChatRWInterface
import br.meetingplace.server.controllers.dependencies.readwrite.community.CommunityRWInterface
import br.meetingplace.server.controllers.dependencies.readwrite.group.GroupRWInterface
import br.meetingplace.server.controllers.dependencies.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.chat.ChatFinderOperator
import br.meetingplace.server.subjects.services.chat.Chat

class ChatReader private constructor() {
    companion object {
        private val Class = ChatReader()
        fun getClass() = Class
    }


    fun seeChat(data: ChatFinderOperator, rwUser: UserRWInterface, rwChat: ChatRWInterface, rwCommunity: CommunityRWInterface, rwGroup: GroupRWInterface): Chat? {
        val user = rwUser.read(data.login.email)

        if (user != null) {
            when (data.identifier.communityGroup || data.identifier.userGroup) {
                true -> { //IS GROUP
                    when (data.identifier.communityGroup) {
                        true -> {//COMMUNITY GROUP
                            val group = rwGroup.read(data.identifier.receiverID)
                            if(group != null){
                                val community = rwCommunity.read(group.getOwner().groupOwnerID)
                                if(community!= null && community.checkGroupApproval(data.identifier.receiverID))
                                    return rwChat.read(group.getChatID())
                            }
                        }
                        false -> { //USER GROUP
                            return rwChat.read(data.identifier.chatID)
                        }
                    }
                }
                false -> { //IS USER <-> USER
                    return rwChat.read(data.identifier.chatID)
                }
            }
        }
        return null
    }
}