package br.meetingplace.management.core

import br.meetingplace.data.user.LoginByEmail
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.core.operators.Verify
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteUser

class Login private constructor(): ReadWriteLoggedUser, ReadWriteUser, Verify{

    companion object{
        private val login = Login()
        fun getLoginSystem() = login
    }

    fun login(newUser: LoginByEmail){ //CASE SENSITIVE
        val logged = readLoggedUser()
        val user = readUser(newUser.email)
        if(user.getAge() >= 16 && logged.email == "" && newUser.password == user.getPassword() && newUser.email == user.getEmail()){
            logged.email = user.getEmail()
            logged.password = user.getPassword()
            writeLoggedUser(logged)
        }
    }

    fun logout(){
        val logged = readLoggedUser()
        logged.email = ""
        logged.password = ""
        writeLoggedUser(logged)
    }
}