package br.meetingplace.management.services.community.dependencies.reader

import br.meetingplace.data.Data
import br.meetingplace.services.community.data.Report
import br.meetingplace.services.thread.MainThread

interface CommunityReaderInterface {
    fun seeReports(data: Data): List<Report>
    fun seeFollowers(data: Data): List<String>
    fun seeModerators(data: Data): List<String>
    fun seeThreads(data: Data): List<MainThread>
    fun seeGroups(data: Data): List<String>
}