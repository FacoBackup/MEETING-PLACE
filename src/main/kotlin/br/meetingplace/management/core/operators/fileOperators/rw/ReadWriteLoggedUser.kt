package br.meetingplace.management.core.operators.fileOperators.rw

import br.meetingplace.data.user.LoginByEmail
import com.google.gson.GsonBuilder
import java.io.File

interface ReadWriteLoggedUser{
    fun readLoggedUser(): LoginByEmail {
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
    fun writeLoggedUser(data: LoginByEmail){
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/logged.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file= File(pathToFile)
        val json = gson.toJson(data)
        file.writeText(json)
    }
}