package br.meetingplace.management.operations.ReadWrite

import br.meetingplace.entities.groups.Group
import br.meetingplace.entities.user.User
import br.meetingplace.servicies.conversationThread.MainThread
import com.google.gson.GsonBuilder
import java.io.File

class ReadWrite private constructor(){
    private val gson = GsonBuilder().setPrettyPrinting().create()

    companion object{
        private val rw = ReadWrite()
        fun getRW () = rw
    }

    fun readUser(fileName: String): User{

        val file = File("$fileName.json")
        val bufferedReader = file.bufferedReader()
        val inputString = bufferedReader.use{ it.readText()}
        return gson.fromJson(inputString, User::class.java)
    }

    fun readGroup(fileName: String): Group {

        val file = File("$fileName.json")
        val bufferedReader = file.bufferedReader()
        val inputString = bufferedReader.use{ it.readText()}
        return gson.fromJson(inputString, Group::class.java)
    }

    fun readThread(fileName: String): MainThread {

        val file = File("$fileName.json")
        val bufferedReader = file.bufferedReader()
        val inputString = bufferedReader.use{ it.readText()}
        return gson.fromJson(inputString, MainThread::class.java)
    }

    fun writeUser(fileName: String,user: User){

        val file = File("$fileName.json")
        val json = gson.toJson(user)
        file.writeText(json)
    }

    fun writeGroup(fileName: String,group: Group){

        val file = File("$fileName.json")
        val json = gson.toJson(group)
        file.writeText(json)
    }

    fun writeThread(fileName: String,thread: MainThread){

        val file = File("$fileName.json")
        val json = gson.toJson(thread)
        file.writeText(json)
    }
}