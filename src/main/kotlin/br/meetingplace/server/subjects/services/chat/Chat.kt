package br.meetingplace.server.subjects.services.chat

import br.meetingplace.server.subjects.services.chat.dependencies.Controller
import br.meetingplace.server.subjects.services.owner.chat.ChatOwnerData

class Chat(private var id: String, private var owner: ChatOwnerData) : Controller() {
    fun getOwner() = owner
    fun getID() = id
}