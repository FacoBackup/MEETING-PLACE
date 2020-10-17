package br.meetingplace.entities.user.profiles

import br.meetingplace.servicies.notification.Inbox

class SocialProfile(
    private var userName: String,
    private var gender: String,
    private var nationality: String,
    private var about: String){

    private var myThreads = mutableListOf<String>()
    private var myChats = mutableListOf<String>()
    var followers = mutableListOf<String>()
    var following = mutableListOf<String>()
    private var inbox = mutableListOf<Inbox>()
    //private var groups = mutableListOf<String>()
    //UPDATE
    fun updateAbout(newAbout: String){
        about = newAbout
    }
    fun updateInbox(notification: Inbox){
        inbox.add(notification)
    }

    fun updateMyChats (id: String) {
        println(userName)
        if (id !in myChats)
            myChats.add(id)
    }

    fun updateMyThreads (id: String, operation: Boolean) {
        when(operation){
            true-> {
                if (id !in myThreads)
                    myThreads.add(id)
            }
            false->{
                if (id in myThreads)
                    myThreads.remove(id)
            }
        }

    }
    //UPDATE

    //GETTERS
    fun getMyChats () = myChats
    fun getMyThreads () = myThreads
    fun getUserName() = userName
    //GETTERS
}