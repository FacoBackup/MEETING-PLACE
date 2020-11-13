package br.meetingplace.server.controllers.dependencies.id.users

interface UserIdsInterface {

    fun fixName(email: String): String

    fun removeSymbolsEmail(email: String): String

}