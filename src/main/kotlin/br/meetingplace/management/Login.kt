package br.meetingplace.management

import br.meetingplace.data.user.LoginByEmail
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser

class Login private constructor(): ReadWriteLoggedUser, ReadWriteUser, Verify {

    companion object{
        private val login = Login()
        fun getLoginSystem() = login
    }

    fun login(newUser: LoginByEmail){ //CASE SENSITIVE
        val logged = readLoggedUser()
        newUser.email = newUser.email.toLowerCase()
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