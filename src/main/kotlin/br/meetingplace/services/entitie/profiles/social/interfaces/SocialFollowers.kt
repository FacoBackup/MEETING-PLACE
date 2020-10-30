package br.meetingplace.services.entitie.profiles.social.interfaces

interface SocialFollowers {
    fun updateFollowers(userEmail: String, remove: Boolean)
    fun updateFollowing(userEmail: String, remove: Boolean)
    fun getFollowing(): List<String>
    fun getFollowers(): List<String>
}