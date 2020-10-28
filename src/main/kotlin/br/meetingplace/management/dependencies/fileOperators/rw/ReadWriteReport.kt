package br.meetingplace.management.dependencies.fileOperators.rw

import br.meetingplace.services.community.data.Report
import com.google.gson.GsonBuilder
import java.io.File

interface ReadWriteReport {
    fun readReport(fileName: String): Report {
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

    fun writeReport(data: Report, fileName: String){
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/communities/reports/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file= File(pathToFile)
        val json = gson.toJson(data)
        file.writeText(json)
    }
}