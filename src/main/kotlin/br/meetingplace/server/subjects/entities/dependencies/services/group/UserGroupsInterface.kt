package br.meetingplace.server.subjects.entities.dependencies.services.group

interface UserGroupsInterface {
    fun updateMyGroups(group: String, delete: Boolean)
    fun updateMemberIn(group: String, leave: Boolean)
    fun getMyGroups(): List<String>
    fun getMemberIn(): List<String>
}