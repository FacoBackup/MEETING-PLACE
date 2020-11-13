package br.meetingplace.server.subjects.services.community.dependencies.data

data class Report(
        val reportID: String,
        val creator: String,
        val serviceID: String,
        val reason: String?,
        var finished: Boolean,
        val communityId: String,
        val response: String?
)

//example:
//idService = threadId
//service = Thread
//reason = "The thread was offensive or something else"
//finished = false
//response = null
//reportId = some random id