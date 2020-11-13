package br.meetingplace.server.subjects.entities.dependencies.preferences

class UserPreferences private constructor() : UserPreferencesInterface {
    companion object {
        private val Class = UserPreferences()
        fun getClass() = Class
    }
}