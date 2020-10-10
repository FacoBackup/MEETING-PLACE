package br.meetingplace.entities.user

open class User(){

    private var id = -1
    private var userEmail= ""
    private var pass= ""
    var age= -1

    //Setters
    fun changeEmail(new: String) {
        userEmail = new
    }

    fun changePass(oldPass: String, newPass: String){
        if(oldPass == pass)
            pass = newPass
    }

    fun updateId (new: Int){
        if(id == -1)
            id = new
    }
    //Setters

    //Getters
    fun getId() = id

    fun getPass() = pass

    fun getEmail() = userEmail
    //Getters
}
