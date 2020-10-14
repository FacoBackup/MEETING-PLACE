package br.meetingplace.data.conversation

data class ChatContent(var message: String, val receiver: Int, val static: Boolean){}