package br.meetingplace.servicies

open class Authentication {

    private var loggedUser = -1

    fun updateLoggedUser(new: Int, previous: Int ){
        if(previous == loggedUser)
            loggedUser = new
    }

    fun getLoggedUser() = loggedUser
}