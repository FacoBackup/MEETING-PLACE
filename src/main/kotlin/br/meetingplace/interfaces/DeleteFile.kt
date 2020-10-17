package br.meetingplace.interfaces

import java.io.File

interface DeleteFile {
    fun delete(file: File){
        if (file.exists())
            file.delete()
    }
}