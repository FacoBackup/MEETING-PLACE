package br.meetingplace.interfaces

import br.meetingplace.data.startup.LoginById
import br.meetingplace.entities.groups.Group
import br.meetingplace.entities.user.User
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.conversationThread.MainThread
import com.google.gson.GsonBuilder
import java.io.File

interface ReadFile {
    fun readUser(fileName: String): User {
        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/users/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File(path)
        val bufferedReader = file.bufferedReader()
        val inputString = bufferedReader.use{ it.readText()}
        return gson.fromJson(inputString, User::class.java)
    }

    fun readGroup(fileName: String): Group {

        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/groups/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File(path)
        val bufferedReader = file.bufferedReader()
        val inputString = bufferedReader.use{ it.readText()}
        return gson.fromJson(inputString, Group::class.java)
    }

    fun readThread(fileName: String): MainThread {
        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/threads/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File(path)
        val bufferedReader = file.bufferedReader()
        val inputString = bufferedReader.use{ it.readText()}
        return gson.fromJson(inputString, MainThread::class.java)
    }
    fun readChat(fileName: String): Chat {
        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/chats/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File(path)
        val bufferedReader = file.bufferedReader()
        val inputString = bufferedReader.use{ it.readText()}
        return gson.fromJson(inputString, Chat::class.java)
    }
    fun readLoggedUser(): LoginById {
        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/logged.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File(path)
        val nullLog = LoginById("", "")
        if(file.exists()){
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use{ it.readText()}
            return gson.fromJson(inputString, LoginById::class.java)
        }
        return nullLog
    }
}
//    fun readEmail(fileName: String): Email {
//
//        val file = File("$fileName.json")
//        val bufferedReader = file.bufferedReader()
//        val inputString = bufferedReader.use{ it.readText()}
//        return gson.fromJson(inputString, Email::class.java)
//    }
//    fun writeEmail(fileName: String,data: Email){
//        val file = File("$fileName.json")
//        val json = gson.toJson(data)
//        file.writeText(json)
//    }