package br.meetingplace.management.dependencies.fileOperators.rw

import br.meetingplace.entitie.User
import com.google.gson.GsonBuilder
import java.io.File

interface ReadWriteUser {
    fun readUser(fileName: String): User {
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
    fun writeUser(data: User, fileName: String){
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/users/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file= File(pathToFile)
        val json = gson.toJson(data)
        file.writeText(json)
    }
}