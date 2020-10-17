package br.meetingplace.interfaces

import br.meetingplace.data.startup.LoginById
import br.meetingplace.entities.groups.Group
import br.meetingplace.entities.user.User
import br.meetingplace.servicies.conversationThread.MainThread
import com.google.gson.GsonBuilder
import java.io.File

interface WriteFile {
    fun writeLoggedUser(data: LoginById){
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File("logged.json")
        val json = gson.toJson(data)
        file.writeText(json)
    }

    fun writeUser(fileName: String,user: User){
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File("$fileName.json")
        val json = gson.toJson(user)
        file.writeText(json)
    }

    fun writeGroup(fileName: String,group: Group){
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File("$fileName.json")
        val json = gson.toJson(group)
        file.writeText(json)
    }

    fun writeThread(fileName: String,thread: MainThread){
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File("$fileName.json")
        val json = gson.toJson(thread)
        file.writeText(json)
    }
}