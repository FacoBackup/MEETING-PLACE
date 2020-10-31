package br.meetingplace.services.entitie
import br.meetingplace.services.entitie.profiles.Profile

class User( private var userName: String,
             private var age: Int,
             private var email: String,
             private var password: String): Profile(){
    fun getPassword() = password
    fun getAge() = age
    fun getEmail() = email
    fun getUserName() = userName
}