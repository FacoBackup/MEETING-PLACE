package br.meetingplace.server.controllers.readwrite.topic

import br.meetingplace.server.subjects.services.topic.Topic
import com.google.gson.GsonBuilder
import java.io.File

class TopicRW private constructor(): TopicRWInterface {
    companion object{
        private val Class = TopicRW()
        fun getClass () = Class
    }

    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun delete(data: Topic) {
        var directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/TOPICS")
        directory += if(data.getMainTopic().isNullOrBlank())
            "/${data.getID()}"
        else
            "/${data.getMainTopic()}"

        try {
            File(directory+"/${data.getID()}.json").delete()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun read(id: String, mainTopic: String?): Topic? {
        var directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/TOPICS")
        directory += if(mainTopic.isNullOrBlank())
            "/$id/$id.json"
        else
            "/$mainTopic/$id.json"

        val file = File(directory)
        var topic: Topic? = null
        try {
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use { it.readText() }
            topic = gson.fromJson(inputString, Topic::class.java)
        } finally {
            return topic
        }
    }

    override fun write(data: Topic) {
        var directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/TOPICS")

        directory += if(data.getMainTopic().isNullOrBlank())
            "/${data.getID()}"
        else
            "/${data.getMainTopic()}"


        val file = "$directory/${data.getID()}.json"


        try {
            val json = gson.toJson(data)
            if (!File(directory).exists())
                File(directory).mkdir()

            File(file).writeText(json)
        } catch (e: Exception) {
            println(e.message)
        }
    }
}