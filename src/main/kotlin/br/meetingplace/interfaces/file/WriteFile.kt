package br.meetingplace.interfaces.file

import br.meetingplace.data.startup.LoginByEmail
import br.meetingplace.servicies.groups.Group
import br.meetingplace.entities.user.User
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.conversationThread.MainThread
import com.google.gson.GsonBuilder
import java.io.File

interface WriteFile {
    fun writeLoggedUser(data: LoginByEmail){
        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/logged.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file= File(path)
        val json = gson.toJson(data)
        file.writeText(json)
    }

    fun writeUser(fileName: String,user: User){
        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/users/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File(path)
        val json = gson.toJson(user)
        file.writeText(json)
    }

    fun writeGroup(fileName: String,group: Group){
        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/groups/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File(path)
        val json = gson.toJson(group)
        file.writeText(json)
    }
    fun writeChat(fileName: String,chat: Chat){
        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/chats/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File(path)
        val json = gson.toJson(chat)
        file.writeText(json)
    }
    fun writeThread(fileName: String,thread: MainThread){
        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/threads/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File(path)
        val json = gson.toJson(thread)
        file.writeText(json)
    }
}