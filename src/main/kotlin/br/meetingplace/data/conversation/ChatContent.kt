package br.meetingplace.data.conversation

data class ChatContent(var message: String, val receiverId: String, val static: Boolean){}