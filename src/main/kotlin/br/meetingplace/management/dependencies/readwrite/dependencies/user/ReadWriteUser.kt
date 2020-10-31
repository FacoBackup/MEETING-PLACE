package br.meetingplace.management.dependencies.readwrite.dependencies.user

import br.meetingplace.services.entitie.User
import com.google.gson.GsonBuilder
import java.io.File
import java.lang.Exception

interface ReadWriteUser {
    fun readUserFromFile(fileName: String): User
    fun writeUser(data: User, fileName: String)
    fun deleteUser(data: User)
}