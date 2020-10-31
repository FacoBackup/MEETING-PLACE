package br.meetingplace.management.dependencies.readwrite.dependencies.group

import br.meetingplace.services.group.Group
import com.google.gson.GsonBuilder
import java.io.File
import java.lang.Exception

class GroupRW private constructor(): ReadWriteGroup{
    companion object{
        private val Class = GroupRW()
        fun getClass()= Class
    }

    override fun readGroup(fileName: String): Group {
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
    override fun writeGroup(data: Group, fileName: String){
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/groups/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file= File(pathToFile)
        val json = gson.toJson(data)
        file.writeText(json)
    }
    override fun deleteGroup(data: Group){
        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/groups/${data.getGroupId()}.json"
        val file = File(path)
        try {
            file.delete()
        }catch (e: Exception){
            println("fileNotFound")
        }
    }
}