package br.meetingplace.management.services.user.dependencies.factory

import br.meetingplace.data.user.UserData

interface UserInterface {
    fun create(newUser: UserData){}
    fun delete(){}
}