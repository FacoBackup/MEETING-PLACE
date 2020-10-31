package br.meetingplace.management.dependencies.readwrite.dependencies.thread

import br.meetingplace.services.thread.MainThread
import com.google.gson.GsonBuilder
import java.io.File
import java.lang.Exception

interface ReadWriteThread {
    fun readThread(fileName: String): MainThread
    fun writeThread(data: MainThread, fileName: String)
    fun deleteThread(data: MainThread)
}