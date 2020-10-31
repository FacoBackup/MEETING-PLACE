package br.meetingplace.management.dependencies.readwrite.dependencies.community

import br.meetingplace.services.community.Community
import com.google.gson.GsonBuilder
import java.io.File
import java.lang.Exception

interface ReadWriteCommunity {
    fun readCommunity(fileName: String): Community
    fun writeCommunity(data: Community, fileName: String)
    fun deleteCommunity(data: Community)
}