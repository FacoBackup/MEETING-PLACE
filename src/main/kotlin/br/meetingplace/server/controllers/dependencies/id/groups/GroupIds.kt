package br.meetingplace.server.controllers.dependencies.id.groups

import br.meetingplace.server.subjects.entities.User

class GroupIds private constructor() : GroupIdsInterface {

    companion object {
        private val Class = GroupIds()
        fun getClass() = Class
    }

    override fun getGroupId(groupName: String, creator: String?): String {

        if (!creator.isNullOrBlank())
            return (groupName.replace("\\s".toRegex(), "") + "_" + (creator.replaceAfter(
                    "@",
                    ""
            )).removeSuffix("@")).toLowerCase()
        return ""
    }

    override fun getGroupSimpleId(id: String): String {
        return (id.replace("\\s".toRegex(), "")).toLowerCase()
    }

    override fun simpleToStandardIdGroup(id: String, user: User): String {
        val memberIn = user.getMemberIn()
        val myGroups = user.getMyGroups()
        val fixedId = getGroupSimpleId(id)

        for (i in memberIn.indices) {
            val idGroup = getGroupId(memberIn[i], memberIn[i])
            if (standardToSimpleIdGroup(getGroupId(memberIn[i], memberIn[i])) == fixedId) {
                return idGroup
            }
        }

        for (i in myGroups.indices) {
            val idGroup = getGroupId(myGroups[i], myGroups[i])
            if (standardToSimpleIdGroup(idGroup) == fixedId) {
                return idGroup
            }
        }
        return ""
    }

    override fun standardToSimpleIdGroup(id: String): String {
        return (id.replaceAfter("_", "")).removeSuffix("_")
    }
}