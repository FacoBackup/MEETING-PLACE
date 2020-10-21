package br.meetingplace.servicies.community

data class Report(val idService: String, val service: String, val reason: String, val finished: Boolean, val response: String){}
//example:
//idService = threadId
//service = Thread
//reason = "The thread was offensive or something else"
//finished = false
//response = null