package br.meetingplace.services.entitie.profiles.social.interfaces

interface SocialCommunities {
    fun updateModeratorIn(id: String,leave:Boolean)
    fun updateCommunitiesIFollow(id: String,unfollow:Boolean)
    fun getModeratorIn(): List<String>
    fun getCommunitiesIFollow(): List<String>
}