package br.meetingplace.management.dependencies.readwrite.dependencies.community

import br.meetingplace.services.community.data.Report
import com.google.gson.GsonBuilder
import java.io.File
import java.lang.Exception

interface ReadWriteReport {
    fun readReport(fileName: String): Report
    fun writeReport(data: Report, fileName: String)
    fun deleteReport(data: Report)
}