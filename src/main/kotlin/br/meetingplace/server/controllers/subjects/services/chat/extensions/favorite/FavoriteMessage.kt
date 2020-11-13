package br.meetingplace.server.controllers.subjects.services.chat.extensions.favorite

import br.meetingplace.server.dto.chat.ChatSimpleOperator

class FavoriteMessage private constructor()  {
    companion object {
        private val Class = FavoriteMessage()
        fun getClass() = Class
    }

    fun userFavoriteMessage(data: ChatSimpleOperator) {
        val user = rw.readUser(data.login.email)
        val receiver = rw.readUser(data.receiver.receiverID)
        val chat = rw.readChat(iDs.getChatId(data.receiver.ownerID, data.receiver.receiverID), data.receiver.ownerID, "", false, community = false)
        if (verify.verifyUser(user) && verify.verifyUser(receiver) && verify.verifyChat(chat)) {

            chat.favoriteMessage(data)
            rw.writeChat(chat)
        }
    }

    fun groupFavoriteMessage(data: ChatSimpleOperator) {
        when (data.receiver.communityGroup) {
            false -> {
                val group = rw.readGroup(data.receiver.receiverID, data.receiver.ownerID, false)
                val user = rw.readUser(data.login.email)

                if (verify.verifyUser(user) && verify.verifyGroup(group) && group.verifyMember(user.getEmail())) {
                    val chat = rw.readChat("", user.getEmail(), group.getGroupID(), group = true, false)
                    chat.favoriteMessage(data)
                    rw.writeChat(chat)
                }
            }
            true -> {
                val group = rw.readGroup(data.receiver.receiverID, data.receiver.ownerID, true)
                val user = rw.readUser(data.login.email)
                val community = rw.readCommunity(data.receiver.ownerID)

                if (verify.verifyUser(user) && verify.verifyGroup(group) && verify.verifyCommunity(community)
                        && community.checkGroupApproval(group.getGroupID()) && group.verifyMember(user.getEmail())) {
                    val chat = rw.readChat("", community.getID(), group.getGroupID(), group = true, true)
                    chat.favoriteMessage(data)
                    rw.writeChat(chat)
                }
            }
        }
    }
}