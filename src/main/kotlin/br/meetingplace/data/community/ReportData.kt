package br.meetingplace.data.community

data class ReportData(val ReportCreator: String, val idService: String, val reason: String?, val communityId: String) {}