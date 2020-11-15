package br.meetingplace.server.subjects.services.chat.dependencies.data

data class MessageContent(var content: String, val ID: String, val creator: String, val type: MessageType)