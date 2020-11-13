package br.meetingplace.server.subjects.entities.dependencies.services.community

interface UserCommunityInterface {
    fun updateModeratorIn(id: String, leave: Boolean)
    fun updateCommunitiesIFollow(id: String, unfollow: Boolean)
    fun getModeratorIn(): List<String>
    fun getCommunitiesIFollow(): List<String>
}