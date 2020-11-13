package br.meetingplace.server.subjects.entities.dependencies.profile

interface UserProfileInterface {
    fun updateProfile(about: String?, nationality: String?, gender: String?)
    fun getGender(): String?
    fun getNationality(): String?
    fun getAbout(): String?
}