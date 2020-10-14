package br.meetingplace.servicies.chat

data class Message(val message: String, val idMessage: Int, val static: Boolean){}
// static == true means that the message will not be removed after a certain amount of time
// static == false means that the message will be destroyed after a defined time