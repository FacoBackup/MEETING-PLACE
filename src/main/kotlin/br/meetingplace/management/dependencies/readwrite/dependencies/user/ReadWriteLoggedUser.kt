package br.meetingplace.management.dependencies.readwrite.dependencies.user

import br.meetingplace.data.user.LoginByEmail
import com.google.gson.GsonBuilder
import java.io.File

interface ReadWriteLoggedUser{
    fun readLoggedUser(): LoginByEmail
    fun writeLoggedUser(data: LoginByEmail)
}