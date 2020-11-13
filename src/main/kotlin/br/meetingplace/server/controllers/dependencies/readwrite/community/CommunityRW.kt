package br.meetingplace.server.controllers.dependencies.readwrite.community

import br.meetingplace.server.subjects.services.community.Community
import com.google.gson.GsonBuilder
import java.io.File

class CommunityRW private constructor(): CommunityRWInterface {
    companion object{
        private val Class = CommunityRW()
        fun getClass () = Class
    }

    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun delete(data: Community) {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/COMMUNITIES/${data.getID()}")
        val file = File(directory)
        try {
            file.delete()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun read(id: String): Community? {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/COMMUNITIES/$id/$id.json")

        val file = File(directory)
        var community: Community? = null
        try {
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use { it.readText() }
            community = gson.fromJson(inputString, Community::class.java)
        } finally {
            return community
        }
    }

    override fun write(data: Community) {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/COMMUNITIES/${data.getID()}")
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