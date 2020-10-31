package br.meetingplace.management.dependencies.readwrite.dependencies.community

import br.meetingplace.services.community.Community
import br.meetingplace.services.community.data.Report
import com.google.gson.GsonBuilder
import java.io.File
import java.lang.Exception

class CommunityRW private constructor(): ReadWriteReport, ReadWriteCommunity{
    companion object{
        private val Class = CommunityRW()
        fun getClass()= Class
    }

    override fun readCommunity(fileName: String): Community {
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

    override fun writeCommunity(data: Community, fileName: String){
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/communities/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file= File(pathToFile)
        val json = gson.toJson(data)
        file.writeText(json)
    }

    override fun deleteCommunity(data: Community){
        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/communities/${data.getId()}.json"
        val file = File(path)
        try {
            file.delete()
        }catch (e: Exception){
            println("fileNotFound")
        }
    }
    override fun readReport(fileName: String): Report {
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/communities/reports/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File(pathToFile)
        var report = Report("", "", "", "", false, "",null)

        try {
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use{ it.readText()}
            report = gson.fromJson(inputString, Report::class.java)
        }finally {
            return report
        }
    }

    override fun writeReport(data: Report, fileName: String){
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/communities/reports/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file= File(pathToFile)
        val json = gson.toJson(data)
        file.writeText(json)
    }
    override fun deleteReport(data: Report){
        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/communities/reports/${data.reportId}.json"
        val file = File(path)
        try {
            file.delete()
        }catch (e: Exception){
            println("fileNotFound")
        }
    }
}