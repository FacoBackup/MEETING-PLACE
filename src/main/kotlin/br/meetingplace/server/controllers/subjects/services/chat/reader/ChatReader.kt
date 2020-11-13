package br.meetingplace.server.controllers.subjects.services.chat.reader

import br.meetingplace.server.dto.chat.ChatFinderOperator
import br.meetingplace.server.subjects.services.chat.Chat

class ChatReader private constructor() {
    companion object {
        private val Class = ChatReader()
        fun getClass() = Class
    }


    fun seeChat(data: ChatFinderOperator): Chat? {
        val user = rw.readUser(data.login.email)

        if (verify.verifyUser(user)) {
            when (data.identifier.communityGroup || data.identifier.userGroup) {
                true -> { //IS GROUP
                    when (data.identifier.communityGroup) {
                        true -> {//COMMUNITY GROUP
                            val community = rw.readCommunity(data.identifier.mainOwnerID)
                            if (verify.verifyCommunity(community) && community.checkGroupApproval(data.identifier.receiverID))
                                return rw.readChat(iDs.getChatId(data.identifier.mainOwnerID, data.identifier.receiverID), data.identifier.mainOwnerID, "", group = true, community = true)
                        }
                        false -> { //USER GROUP
                            return rw.readChat("", user.getEmail(), data.identifier.receiverID, group = true, community = false)
                        }
                    }
                }
                false -> { //IS USER <-> USER
                    return rw.readChat(iDs.getChatId(data.identifier.mainOwnerID, data.identifier.receiverID), data.identifier.mainOwnerID, "", group = false, community = false)
                }
            }
        }
        return null
    }
}