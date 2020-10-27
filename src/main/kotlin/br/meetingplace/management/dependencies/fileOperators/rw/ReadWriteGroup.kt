package br.meetingplace.management.dependencies.fileOperators.rw

import br.meetingplace.services.group.Group
import com.google.gson.GsonBuilder
import java.io.File

interface ReadWriteGroup {
    fun readGroup(fileName: String): Group {
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/groups/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File(pathToFile)
        var group = Group()

        try {
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use{ it.readText()}
            group = gson.fromJson(inputString, Group::class.java)
        }finally {
            return group
        }
    }
    fun writeGroup(data: Group, fileName: String){
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/groups/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file= File(pathToFile)
        val json = gson.toJson(data)
        file.writeText(json)
    }
}