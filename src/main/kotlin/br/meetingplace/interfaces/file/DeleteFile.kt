package br.meetingplace.interfaces.file

import java.io.File

interface DeleteFile {
    fun delete(file: File){
        if (file.exists())
            file.delete()
    }
}