package br.meetingplace.servicies.notificacao

class NotificacaoChat: Notificacao() {
    var chat = mutableListOf<Int>() //Id do Usuario que enviou a mensagem
}