package br.meetingplace.management.core.operators.fileOperators.rw

import br.meetingplace.services.thread.MainThread
import com.google.gson.GsonBuilder
import java.io.File

interface ReadWriteThread {
    fun readThread(fileName: String): MainThread {
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/threads/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = File(pathToFile)
        var thread = MainThread()

        try {
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use{ it.readText()}
            thread = gson.fromJson(inputString, MainThread::class.java)
        }finally {
            return thread
        }
    }
    fun writeThread(data: MainThread, fileName: String){
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/threads/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file= File(pathToFile)
        val json = gson.toJson(data)
        file.writeText(json)
    }
}