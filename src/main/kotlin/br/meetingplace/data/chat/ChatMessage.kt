package br.meetingplace.data.chat

data class ChatMessage(var message: String, val idReceiver: String, val static: Boolean){}