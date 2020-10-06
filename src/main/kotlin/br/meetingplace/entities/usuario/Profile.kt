package br.meetingplace.entities.usuario

import br.meetingplace.servicies.chat.Chat

class Profile(): User(){

    var userName: String? = null
    var gender: String? = null
    var nacionality: String? = null
    var about: String? = null
    private var groupRole = mutableListOf<Int>()

    fun getGroupRole() = groupRole
    var chat = mutableListOf<Chat>()
    var friends = mutableListOf<Int>()
    var groups = mutableListOf<Int>()
}