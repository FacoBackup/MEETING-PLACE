package br.meetingplace.management.dependencies.readwrite.dependencies.user

import br.meetingplace.services.entitie.User
import com.google.gson.GsonBuilder
import java.io.File
import java.lang.Exception

interface ReadWriteUser {
    fun readUser(fileName: String): User
    fun writeUserToFile(data: User, fileName: String)
    fun deleteUser(data: User)
}