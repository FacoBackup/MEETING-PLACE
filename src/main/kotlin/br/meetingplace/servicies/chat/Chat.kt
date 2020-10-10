package br.meetingplace.servicies.chat

open class Chat(ID: Int){
    var conversation = mutableListOf<String>()// Conversa vai aqui
    private var id = ID //Id da conversa é a soma dos ids dos usuarios

    fun getId() = id
}