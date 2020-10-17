package br.meetingplace.interfaces

import br.meetingplace.data.startup.LoginById
import br.meetingplace.entities.groups.Group
import br.meetingplace.entities.user.User
import br.meetingplace.servicies.conversationThread.MainThread
import com.google.gson.GsonBuilder
import java.io.File

interface ReadFile {
    fun readUser(fileName: String): User {

        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File("$fileName.json")
        val bufferedReader = file.bufferedReader()
        val inputString = bufferedReader.use{ it.readText()}
        return gson.fromJson(inputString, User::class.java)
    }

    fun readGroup(fileName: String): Group {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File("$fileName.json")
        val bufferedReader = file.bufferedReader()
        val inputString = bufferedReader.use{ it.readText()}
        return gson.fromJson(inputString, Group::class.java)
    }

    fun readThread(fileName: String): MainThread {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File("$fileName.json")
        val bufferedReader = file.bufferedReader()
        val inputString = bufferedReader.use{ it.readText()}
        return gson.fromJson(inputString, MainThread::class.java)
    }



    fun readLoggedUser(): LoginById {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File("logged.json")
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