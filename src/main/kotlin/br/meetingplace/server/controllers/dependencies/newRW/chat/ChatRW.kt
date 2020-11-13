package br.meetingplace.server.controllers.dependencies.newRW.chat

import br.meetingplace.server.subjects.services.chat.Chat
import br.meetingplace.server.subjects.services.community.Community
import br.meetingplace.server.subjects.services.owner.OwnerType
import br.meetingplace.server.subjects.services.owner.chat.ChatOwnerData
import com.google.gson.GsonBuilder
import java.io.File

class ChatRW: ChatRWInterface{
    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun delete(data: Chat) {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/CHATS/${data.getID()}.json")
        val file = File(directory)
        try {
            file.delete()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun read(id: String): Chat{
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/CHATS/$id.json")

        val file = File(directory)
        var chat = Chat("", ChatOwnerData("", "", OwnerType.COMMUNITY, OwnerType.GROUP))
        try {
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use { it.readText() }
            chat = gson.fromJson(inputString, Chat::class.java)
        } finally {
            return chat
        }
    }

    override fun write(data: Chat) {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/CHATS/${data.getID()}.json")
        try {
            val file = File(directory)
            val json = gson.toJson(data)

            if (!File(directory).exists())
                File(directory).mkdir()

            file.writeText(json)
        } catch (e: Exception) {
            println(e.message)
        }
    }
}