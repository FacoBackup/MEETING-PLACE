package br.meetingplace.management.services.search.dependecies.user

class UserSearch private constructor(): UserSearchInterface{

    companion object{
        private val Class = UserSearch()
        fun getClass() = Class
    }

}