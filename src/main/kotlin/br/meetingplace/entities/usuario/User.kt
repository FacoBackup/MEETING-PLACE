package br.meetingplace.entities.usuario

import br.meetingplace.application.UserManagement
import br.meetingplace.servicies.Authentication

open class User(): Authentication(){

    private var id = -1
    private var name: String? = null
    private var userEmail: String? = null
    private var pass: String? = null
    val management =  UserManagement()
    var age: Int? = null


    //Update
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
    //Update

    //Getters
    fun getId() = id

    fun getPass() = pass

    fun getName() = name

    fun getEmail() = userEmail
    //Getters
}
