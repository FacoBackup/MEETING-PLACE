package br.meetingplace.server.subjects.entities.dependencies.services.group

class UserGroups private constructor() : UserGroupsInterface {
    companion object {
        private val Class = UserGroups()
        fun getClass() = Class
    }

    private var myGroups = mutableListOf<String>()
    private var memberIn = mutableListOf<String>()

    override fun getMyGroups() = myGroups
    override fun getMemberIn() = memberIn
    override fun updateMemberIn(group: String, leave: Boolean) {
        when (leave) {
            true -> {
                if (group in memberIn)
                    memberIn.remove(group)
            }
            false -> {
                if (group !in memberIn)
                    memberIn.add(group)
            }
        }
    }

    override fun updateMyGroups(group: String, delete: Boolean) {
        when (delete) {
            true -> {
                if (group in myGroups)
                    myGroups.remove(group)
            }
            false -> {
                if (group !in myGroups)
                    myGroups.add(group)
            }
        }
    }
}