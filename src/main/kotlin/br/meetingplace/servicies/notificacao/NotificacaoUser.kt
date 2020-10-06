package br.meetingplace.servicies.notificacao

class NotificacaoUser: Notificacao() {
    var user = mutableListOf<String>() //Nome do usuario
    var userId= mutableListOf<Int>() //Id do usuario
}