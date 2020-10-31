package br.meetingplace.management.dependencies.idmanager.dependencies.users

interface UserIdsInterface {

    fun fixEmail(email: String): String
//    fun attachNameToEmail(name: String, email: String): String
//    fun getEmailByAttachedNameToEmail(attached: String): String
//    fun getNameByAttachedNameToEmail(attached: String): String
}