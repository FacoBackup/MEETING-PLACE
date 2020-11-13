package br.meetingplace.server.controllers.dependencies.newRW.user

import br.meetingplace.server.subjects.entities.User
import com.google.gson.GsonBuilder
import java.io.File

class UserRW: UserRWInterface {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun delete(data: User) {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/USERS/${data.getEmail()}")
        val file = File(directory)
        try {
            file.delete()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun read(id: String): User {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/USERS/$id/$id.json")

        val file = File(directory)
        var user = User("", -1, "", "")
        try {
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use { it.readText() }
            user = gson.fromJson(inputString, User::class.java)
        } finally {
            return user
        }
    }

    override fun write(data: User) {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/USERS/${data.getEmail()}")
        val jsonFile = "$directory/${data.getEmail()}.json"
        try {
            val file = File(jsonFile)
            val json = gson.toJson(data)

            if (!File(directory).exists())
                File(directory).mkdir()

            file.writeText(json)
        } catch (e: Exception) {
            println(e.message)
        }
    }
}