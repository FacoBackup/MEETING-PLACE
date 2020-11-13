package br.meetingplace.server.subjects.services.groups

import br.meetingplace.server.subjects.services.members.Members
import br.meetingplace.server.subjects.services.members.data.MemberData
import br.meetingplace.server.subjects.services.members.data.MemberType
import br.meetingplace.server.subjects.services.owner.group.GroupOwnerData

class Group(private val owner: GroupOwnerData, private val groupID: String, private val name: String, private val chatID: String) : Members() {
    private var about: String? = null

    fun getOwner() = owner
    fun getNameGroup() = name
    fun getGroupID() = groupID
    fun getChatID() = chatID

    init {
        startMembers(MemberData(owner.groupCreatorID, MemberType.CREATOR))
    }

    fun updateAbout(about: String) {
        this.about = about
    }
}
