package br.meetingplace.data.chat.users

data class ChatNewMessage(var message: String, val receiverId: String, val static: Boolean){}