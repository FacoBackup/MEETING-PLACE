package br.meetingplace.entities.usuario.profiles

import br.meetingplace.entities.usuario.User
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.notification.Inbox

class SocialProfile(): User(){

    var userName = ""
    var gender= ""
    var nacionality= ""
    var about= ""

    var chat = mutableListOf<Chat>()
    var followers = mutableListOf<Int>()
    var following = mutableListOf<Int>()
    var groups = mutableListOf<Int>()
    var inbox = mutableListOf<Inbox>()

}