package br.meetingplace.server.subjects.services.chat.dependencies.data

data class MessageContent(var message: String, val messageID: String, val creator: String, val type: MessageType)