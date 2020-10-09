package br.meetingplace.entities.usuario

import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.notificacao.Inbox

class Profile(): User(){

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