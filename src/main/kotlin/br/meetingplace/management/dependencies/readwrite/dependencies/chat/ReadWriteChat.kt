package br.meetingplace.management.dependencies.readwrite.dependencies.chat

import br.meetingplace.services.chat.Chat
import br.meetingplace.services.community.data.Report
import com.google.gson.GsonBuilder
import java.io.File
import java.lang.Exception

interface ReadWriteChat {
    fun readChat(fileName: String): Chat
    fun writeChat(data: Chat, fileName: String)
    fun deleteChat(data: Chat)
}