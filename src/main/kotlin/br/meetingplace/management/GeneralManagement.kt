package br.meetingplace.management

import br.meetingplace.data.startup.LoginByEmail
import br.meetingplace.data.startup.LoginById
import br.meetingplace.entities.user.User
import br.meetingplace.interfaces.Generator
import br.meetingplace.management.operations.ReadWrite.ReadWrite
import br.meetingplace.management.operations.verifiers.UserVerifiers
import java.io.File

// CHANGE TO INTERFACE
open class GeneralManagement : Generator{

    private val rw = ReadWrite.getRW()
    private var logged = ""
    private var cachedPass = ""
    fun getLoggedUser() = logged
    fun getCachedPassword() = cachedPass

    companion object{
        private val management = GeneralManagement()
        fun getManagement()= management
    }

    //AUTHENTICATION SYSTEM
    fun loginId(log: LoginById){
        val fileUser = File("$management.json").exists()

        if(fileUser){
            val user = rw.readUser(log.user)
            if(log.password == user.getPassword()){
                logged = log.user
                cachedPass = log.password
            }
        }
    }

    fun logoff(){

        if(logged != ""){
            logged = ""
            cachedPass = ""
        }
    }
    //AUTHENTICATION SYSTEM
}