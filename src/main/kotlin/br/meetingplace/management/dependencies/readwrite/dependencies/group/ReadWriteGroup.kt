package br.meetingplace.management.dependencies.readwrite.dependencies.group

import br.meetingplace.services.group.Group
import com.google.gson.GsonBuilder
import java.io.File
import java.lang.Exception

interface ReadWriteGroup {
    fun readGroup(fileName: String): Group
    fun writeGroup(data: Group, fileName: String)
    fun deleteGroup(data: Group)
}