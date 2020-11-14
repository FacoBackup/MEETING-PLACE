package br.meetingplace.server.subjects.entities.dependencies.preferencesTODO

class UserPreferences private constructor() : UserPreferencesInterface {
    companion object {
        private val Class = UserPreferences()
        fun getClass() = Class
    }
}