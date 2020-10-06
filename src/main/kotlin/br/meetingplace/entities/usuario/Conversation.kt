package br.meetingplace.entities.usuario

class Conversation(Msg: String, Sender: Int, Receiver: Int){

    var message = Msg
    var receiver = Receiver
    var sender = Sender
}