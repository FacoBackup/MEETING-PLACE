package br.meetingplace.entities.grupos

class GroupConversation(Msg: String, Sender: Int, Group: Int){
    var message = Msg
    var sender = Sender
    var group = Group
}