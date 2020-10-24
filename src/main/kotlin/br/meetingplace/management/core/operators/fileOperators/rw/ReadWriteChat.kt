package br.meetingplace.management.core.operators.fileOperators.rw

import br.meetingplace.services.chat.Chat
import com.google.gson.GsonBuilder
import java.io.File

interface ReadWriteChat {
    fun readChat(fileName: String): Chat {
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
    fun writeChat(data: Chat, fileName: String){
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/chats/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file= File(pathToFile)
        val json = gson.toJson(data)
        file.writeText(json)
    }
}