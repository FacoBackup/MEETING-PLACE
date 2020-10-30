package br.meetingplace.services.entitie.profiles.social.interfaces

interface SocialChats {
    fun updateMyChats (id: String)
    fun getMyChats (): List<String>
}