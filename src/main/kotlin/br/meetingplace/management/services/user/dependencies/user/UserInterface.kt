package br.meetingplace.management.services.user.dependencies.user

import br.meetingplace.data.user.UserData

interface UserInterface {
    fun create(newUser: UserData){}
    fun delete(){}
}