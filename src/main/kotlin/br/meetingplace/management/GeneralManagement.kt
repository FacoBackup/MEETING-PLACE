package br.meetingplace.management

import br.meetingplace.data.startup.LoginByEmail
import br.meetingplace.interfaces.utility.Generator
import br.meetingplace.interfaces.file.ReadFile
import br.meetingplace.interfaces.file.WriteFile
import java.io.File

// CHANGE TO INTERFACE
open class GeneralManagement private constructor(): ReadFile, WriteFile, Generator {

    companion object{
        private val management = GeneralManagement()
        fun getManagement()= management
    }

    //AUTHENTICATION SYSTEM
    fun login(log: LoginByEmail){
        val pathUser = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/users/${log.email}.json"
        val pathLogged = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/logged.json"

        val fileUser = File(pathUser).exists()
        val fileLog = File(pathLogged).exists()

        if(fileUser && fileLog){
            val user = readUser(log.email)
            val loggedFile = readLoggedUser()
            if(log.password == user.getPassword() && loggedFile.email == "")
                writeLoggedUser(log)
        }
        else if (fileUser && !fileLog){
            val user = readUser(log.email)
            if(log.password == user.getPassword())
                writeLoggedUser(log)
        }
    }

    fun logoff(){
        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/logged.json"
        val fileLog = File(path).exists()
        if(fileLog){

            val log = readLoggedUser()
            if(log.email != ""){
                log.email = ""
                log.password = ""
                writeLoggedUser(log)
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