package br.meetingplace.management.core.operators.fileOperators.rw

import br.meetingplace.services.community.Community
import br.meetingplace.services.group.Group
import com.google.gson.GsonBuilder
import java.io.File

interface ReadWriteCommunity {
    fun readCommunity(fileName: String): Community {
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/communities/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File(pathToFile)
        var community = Community.getCommunity()

        try {
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use{ it.readText()}
            community = gson.fromJson(inputString, Community::class.java)
        }finally {
            return community
        }
    }

    fun writeCommunity(data: Community, fileName: String){
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/communities/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file= File(pathToFile)
        val json = gson.toJson(data)
        file.writeText(json)
    }
}