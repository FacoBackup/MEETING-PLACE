package br.meetingplace.management.services

import br.meetingplace.data.user.LoginByEmail
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.services.entitie.User

class Login private constructor(){
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()
    private val iDs = IDsController.getClass()

    companion object{
        private val login = Login()
        fun getLoginSystem() = login
    }

    fun login(newUser: LoginByEmail){ //CASE SENSITIVE
        val logged = rw.readLoggedUser()
        lateinit var user: User
        newUser.email = newUser.email.toLowerCase()
        newUser.fileName = iDs.attachNameToEmail(newUser.userName,newUser.email)

        if(newUser.fileName.isNullOrBlank() && logged.email.isNotBlank() && newUser.userName.isNotBlank()){
            user = rw.readUser(newUser.fileName!!)
            when(verify.verifyUser(rw.readUser(newUser.fileName!!)) && user.getAge() >= 16 && newUser.password == user.getPassword()){
                true->{
                    logged.email = user.getEmail()
                    logged.password = user.getPassword()
                    rw.writeLoggedUser(logged)
                }
            }
        }
    }

    fun logout(){
        val logged = rw.readLoggedUser()
        logged.email = ""
        logged.password = ""
        logged.fileName = null
        logged.userName = ""
        rw.writeLoggedUser(logged)
    }
}