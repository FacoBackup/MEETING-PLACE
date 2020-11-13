package br.meetingplace.server.controllers.dependencies.newRW.topic.main

import br.meetingplace.server.subjects.services.owner.OwnerType
import br.meetingplace.server.subjects.services.owner.topic.TopicOwnerData
import br.meetingplace.server.subjects.services.topic.Topic
import com.google.gson.GsonBuilder
import java.io.File

class TopicRW: TopicRWInterface {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun delete(data: Topic) {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/TOPICS/${data.getID()}")
        val file = File(directory)
        try {
            file.delete()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun read(id: String): Topic {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/TOPICS/$id/$id.json")

        val file = File(directory)
        var topic = Topic(TopicOwnerData("", "", OwnerType.COMMUNITY), "", "", "")
        try {
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use { it.readText() }
            topic = gson.fromJson(inputString, Topic::class.java)
        } finally {
            return topic
        }
    }

    override fun write(data: Topic) {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/TOPICS/${data.getID()}")
        val jsonFile = "$directory/${data.getID()}.json"
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