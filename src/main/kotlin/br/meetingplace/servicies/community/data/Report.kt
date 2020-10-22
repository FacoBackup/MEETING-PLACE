package br.meetingplace.servicies.community.data

data class Report(val reportId: String, val creator: String, val idService: String, val reason: String, var finished: Boolean, val response: String){}
//example:
//idService = threadId
//service = Thread
//reason = "The thread was offensive or something else"
//finished = false
//response = null
//reportId = some random id