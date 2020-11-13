package br.meetingplace.server.subjects.entities

import br.meetingplace.server.subjects.entities.dependencies.Controller

class User(
        private var userName: String,
        private var age: Int,
        private var email: String,
        private var password: String
) : Controller() {

    fun getPassword() = password
    fun getAge() = age
    fun getEmail() = email
    fun getUserName() = userName
}