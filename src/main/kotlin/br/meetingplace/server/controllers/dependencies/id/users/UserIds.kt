package br.meetingplace.server.controllers.dependencies.id.users

class UserIds private constructor() : UserIdsInterface {

    companion object {
        private val Class = UserIds()
        fun getClass() = Class
    }

    override fun fixName(email: String): String {
        return (email.replace("\\s".toRegex(), "")).toLowerCase()
    }

    override fun removeSymbolsEmail(email: String): String {
        return (email.replaceAfter("@", "")).removeSuffix("@")
    }
}