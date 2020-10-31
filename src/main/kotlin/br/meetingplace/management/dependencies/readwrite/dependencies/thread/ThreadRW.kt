package br.meetingplace.management.dependencies.readwrite.dependencies.thread

import br.meetingplace.services.thread.MainThread
import com.google.gson.GsonBuilder
import java.io.File
import java.lang.Exception

class ThreadRW private constructor(): ReadWriteThread{
    companion object{
        private val Class = ThreadRW()
        fun getClass()= Class
    }
    override fun readThread(fileName: String): MainThread {
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
    override fun writeThread(data: MainThread, fileName: String){
        val pathToFile = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/threads/$fileName.json"
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file= File(pathToFile)
        val json = gson.toJson(data)
        file.writeText(json)
    }
    override fun deleteThread(data: MainThread){
        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/threads/${data.getId()}.json"
        val file = File(path)
        try {
            file.delete()
        }catch (e: Exception){
            println("fileNotFound")
        }
    }
}