package br.meetingplace.servicies.conversationThread


// NEEDS WORK (DATA CLASS)
data class SubThread(
    var likes: MutableList<String> ,
    var dislikes: MutableList<String>,
    val creator: String,
    var header: String,
    var body: String,
    var footer: String,
    val id: String
){}