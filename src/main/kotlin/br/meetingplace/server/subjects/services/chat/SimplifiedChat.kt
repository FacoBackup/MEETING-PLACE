package br.meetingplace.server.subjects.services.chat

import br.meetingplace.server.subjects.services.owner.chat.ChatOwnerData

data class SimplifiedChat(val owner: ChatOwnerData, val user: String)