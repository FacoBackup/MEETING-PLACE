package br.meetingplace.server.controllers.readwrite.report

import br.meetingplace.server.subjects.services.community.dependencies.data.Report

interface ReportRWInterface {
    fun delete(data: Report)
    fun read(id: String): Report?
    fun write(data: Report)
}