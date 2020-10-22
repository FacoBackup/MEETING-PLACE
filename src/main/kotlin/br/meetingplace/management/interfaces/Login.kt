package br.meetingplace.management.interfaces

import br.meetingplace.data.user.LoginByEmail
import br.meetingplace.management.interfaces.file.ReadFile
import br.meetingplace.management.interfaces.file.WriteFile
import br.meetingplace.management.interfaces.utility.Generator
import java.io.File

interface Login: ReadFile, WriteFile, Generator {

    fun login(log: LoginByEmail){ //CASE SENSITIVE
        val pathUser = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/users/${log.email}.json"
        val pathLogged = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/logged.json"

        val fileUser = File(pathUser).exists()
        val fileLog = File(pathLogged).exists()
        println("step 1")
        println(fileUser)
        println(fileLog)
        if(fileUser && fileLog){
            println("step 2a")
            val user = readUser(log.email)
            val loggedFile = readLoggedUser()
            if(log.password == user.getPassword() && loggedFile.email == "")
                writeLoggedUser(log)
        }
        else if (fileUser && !fileLog){
            println("step 2b")
            val user = readUser(log.email)
            if(log.password == user.getPassword())
                writeLoggedUser(log)
        }
    }
    fun logout(){
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
}