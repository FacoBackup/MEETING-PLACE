package br.meetingplace.server.subjects.services.community

import br.meetingplace.server.subjects.services.community.dependencies.Controller
import br.meetingplace.server.subjects.services.members.data.MemberData
import br.meetingplace.server.subjects.services.members.data.MemberType


class Community(private val name: String, private val id: String, private var about: String?, creator: String) : Controller() {
    fun getName() = name
    fun getID() = id

    init {
        startMembers(MemberData(creator, MemberType.CREATOR))
    }

    fun updateModerator(data: String, requester: String, remove: Boolean?) {
        if (getMemberRole(requester) == MemberType.MODERATOR || getMemberRole(requester) == MemberType.CREATOR) {
            when (remove) {
                true -> { //REMOVE
                    if (verifyMember(data) && getMemberRole(requester) == MemberType.CREATOR)
                        updateMember(MemberData(data, MemberType.MODERATOR), true)
                }
                false -> { //ADD
                    if (data !in getModerators())
                        updateMember(MemberData(data, MemberType.MODERATOR), false)
                }
                null -> { //STEP-DOWN
                    if (data in getModerators()) {
                        updateMember(MemberData(data, MemberType.NORMAL), false)
                        updateFollower(data, false)
                    }
                }
            }
        }
    }

    fun updateFollower(data: String, remove: Boolean) {
        when (remove) {
            false -> {
                if (!verifyMember(data)) {
                    updateMember(MemberData(data, MemberType.NORMAL), false)
                }
            }
            true -> {
                if (verifyMember(data)) {
                    updateMember(MemberData(data, MemberType.NORMAL), true)
                }
            }
        }
    }

}