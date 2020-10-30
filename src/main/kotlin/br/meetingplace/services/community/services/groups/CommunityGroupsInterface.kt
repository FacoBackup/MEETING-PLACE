package br.meetingplace.services.community.services.groups

interface CommunityGroupsInterface {
    fun getApprovedGroups(): List<String>
    fun getGroupsInValidation(): List<String>
    fun checkGroupApproval(id: String): Boolean
    fun removeApprovedGroup(group: String)
    fun updateGroupsInValidation(group: String,approve: Boolean?)
}