package br.meetingplace.management.dependencies.readwrite.dependencies.user

import br.meetingplace.data.user.LoginByEmail
import br.meetingplace.services.entitie.User
import com.google.gson.GsonBuilder
import java.io.File
import java.lang.Exception

class UserRW private constructor(): ReadWriteLoggedUser, ReadWriteUser{
    companion object{
        private val Class = UserRW()
        fun getClass()= Class
    }

    override fun readLoggedUser(): LoginByEmail {
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/logged.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File(pathToFile)
        var data = LoginByEmail("", "")
        try {
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use{ it.readText()}
            data = gson.fromJson(inputString, LoginByEmail::class.java)
        }finally {
            return data
        }
    }

    override fun writeLoggedUser(data: LoginByEmail){
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/logged.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file= File(pathToFile)
        val json = gson.toJson(data)
        file.writeText(json)
    }
    override fun readUser(fileName: String): User {
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/users/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File(pathToFile)
        var user = User("", -1, "", "")
        try {
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use{ it.readText()}
            user = gson.fromJson(inputString, User::class.java)
        }finally {
            return user
        }
    }
    override fun writeUserToFile(data: User, fileName: String){
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/users/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file= File(pathToFile)
        val json = gson.toJson(data)
        file.writeText(json)
    }
    override fun deleteUser(data: User){
        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/groups/${data.getEmail()}.json"
        val file = File(path)
        try {
            file.delete()
        }catch (e: Exception){
            println("fileNotFound")
        }
    }
}