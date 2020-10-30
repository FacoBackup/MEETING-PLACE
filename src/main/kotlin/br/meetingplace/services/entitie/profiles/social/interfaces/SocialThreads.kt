package br.meetingplace.services.entitie.profiles.social.interfaces



interface SocialThreads {
    fun clearNotifications()
    fun updateMyThreads (id: String, add: Boolean)
    fun getMyThreads (): List<String>
}