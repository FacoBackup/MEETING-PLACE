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

    fun readUser(): User? {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File("br/meetingplace/logFiles/Users.json")
        val bufferedReader = file.bufferedReader()
        val inputString = bufferedReader.use{ it.readText()}
        return gson.fromJson(inputString, User::class.java)
    }
    fun readGroup(): Group? {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File("br/meetingplace/logFiles/groups.json")
        val bufferedReader = file.bufferedReader()
        val inputString = bufferedReader.use{ it.readText()}
        return gson.fromJson(inputString, Group::class.java)
    }
    fun readThread(): MainThread? {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File("br/meetingplace/logFiles/thread.json")
        val bufferedReader = file.bufferedReader()
        val inputString = bufferedReader.use{ it.readText()}
        return gson.fromJson(inputString, MainThread::class.java)
    }

    fun write(nameFile: String){
        val file = File("$nameFile.json")
        val json = gson.toJson(this)
        file.writeText(json)
    }
}