package br.meetingplace.management.dependencies.readwrite.dependencies.chat

import br.meetingplace.services.chat.Chat
import com.google.gson.GsonBuilder
import java.io.File
import java.lang.Exception

class ChatRW private constructor(): ReadWriteChat {
    companion object {
        private val Class = ChatRW()
        fun getClass() = Class
    }
    override fun readChat(fileName: String): Chat {
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/chats/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File(pathToFile)
        var chat = Chat.getChat()

        try {
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use{ it.readText()}
            chat = gson.fromJson(inputString, Chat::class.java)
        }finally {
            return chat
        }
    }
    override fun writeChat(data: Chat, fileName: String){
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/chats/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file= File(pathToFile)
        val json = gson.toJson(data)
        file.writeText(json)
    }
    override fun deleteChat(data: Chat){
        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/communities/reports/${data.getConversationId()}.json"
        val file = File(path)
        try {
            file.delete()
        }catch (e: Exception){
            println("fileNotFound")
        }
    }
}