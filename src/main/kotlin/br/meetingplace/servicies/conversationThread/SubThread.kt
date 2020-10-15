package br.meetingplace.servicies.conversationThread


// NEEDS WORK (DATA CLASS)
data class SubThread(
    var likes: MutableList<Int> ,
    var dislikes: MutableList<Int>,
    val creator: Int,
    var header: String,
    var body: String,
    var footer: String,
    val id: Int
){}