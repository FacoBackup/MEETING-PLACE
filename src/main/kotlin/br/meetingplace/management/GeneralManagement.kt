package br.meetingplace.management

import br.meetingplace.data.startup.LoginById
import br.meetingplace.interfaces.Generator
import br.meetingplace.interfaces.ReadFile
import br.meetingplace.interfaces.WriteFile
import java.io.File

// CHANGE TO INTERFACE
open class GeneralManagement private constructor(): ReadFile, WriteFile,Generator{

    companion object{
        private val management = GeneralManagement()
        fun getManagement()= management
    }

    //AUTHENTICATION SYSTEM
    fun loginId(log: LoginById){

        val fileUser = File("${log.user}.json").exists()
        val fileLog = File("logged.json").exists()
        println(fileUser)
        println(log.user)
        if(fileUser && fileLog){
            val user = readUser(log.user)
            val loggedFile = readLoggedUser()
            if(log.password == user.getPassword() && loggedFile.user == ""){
                writeLoggedUser(log)
                println("done")
            }

        }
        else if (fileUser && !fileLog){

            println("ELSE if")
            val user = readUser(log.user)
            if(log.password == user.getPassword())
                writeLoggedUser(log)
        }
        println("nothing")
    }

    fun logoff(){

        val fileLog = File("logged.json").exists()
        println(fileLog)
        if(fileLog){

            val log = readLoggedUser()
            if(log.user != ""){
                println("Here")
                log.user = ""
                log.password = ""
                writeLoggedUser(log)
                println("Done")
            }

        }
    }
    //AUTHENTICATION SYSTEM



//    fun loginEmail(log: LoginByEmail){
//        val fileEmail = File("${log.email}.json").exists()
//
//        if(fileEmail){
//            val email = readEmail(log.email)
//            val user = readUser(email.userId)
//
//            if(log.password == user.getPassword()){
//                logged = user.getId()
//                cachedPass = log.password
//            }
//        }
//    }

}