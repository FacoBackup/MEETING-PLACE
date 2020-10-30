package br.meetingplace.services.entitie.profiles.social.interfaces

interface SocialGroups {
    fun updateMyGroups(idGroup: String, delete: Boolean)
    fun updateMemberIn(idGroup: String, leave: Boolean)
    fun getMyGroups (): List<String>
    fun getMemberIn(): List<String>
}