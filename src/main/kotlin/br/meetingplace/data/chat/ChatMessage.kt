package br.meetingplace.data.chat

data class ChatMessage(var message: String, val idReceiver: String, val static: Boolean, val creator: String?,val idCommunity:String?){}