package br.meetingplace.server.subjects.services.members

import br.meetingplace.server.subjects.services.members.data.MemberData
import br.meetingplace.server.subjects.services.members.data.MemberType

abstract class Members {


    private val members = mutableListOf<MemberData>()

    fun getMembers(): List<String> {
        val members = mutableListOf<String>()
        for (i in 0 until members.size) {
            if (this.members[i].role == MemberType.MODERATOR)
                members.add(this.members[i].userEmail)
        }
        return members
    }

    fun getModerators(): List<String> {
        val mods = mutableListOf<String>()
        for (i in 0 until members.size) {
            if (members[i].role == MemberType.MODERATOR)
                mods.add(members[i].userEmail)
        }
        return mods
    }

    fun startMembers(data: MemberData) {
        if (getCreator() == "")
            members.add(data)
    }

    fun updateMember(member: MemberData, remove: Boolean) {
        when (remove) {
            true -> {
                if (verifyMember(member.userEmail))
                    members.remove(member)
            }
            false -> {
                if (!verifyMember(member.userEmail))
                    members.add(member)
            }
        }
    }

    fun getCreator(): String {
        for (i in 0 until members.size) {
            if (members[i].role == MemberType.CREATOR)
                return members[i].userEmail
        }
        return ""
    }

    fun updateMemberRole(email: String, newRole: MemberType) {
        val index = getMemberIndex(email)
        if (index != -1)
            members[index].role = newRole
    }

    fun getMemberRole(email: String): MemberType? {
        val index = getMemberIndex(email)
        return if (index != -1) members[index].role else null
    }

    fun verifyMember(email: String): Boolean {
        for (i in 0 until members.size) {
            if (members[i].userEmail == email)
                return true
        }
        return false
    }

    private fun getMemberIndex(email: String): Int {
        for (i in 0 until members.size) {
            if (members[i].userEmail == email)
                return i
        }
        return -1
    }
}