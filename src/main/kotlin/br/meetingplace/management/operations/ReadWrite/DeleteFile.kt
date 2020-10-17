package br.meetingplace.management.operations.ReadWrite

import java.io.File

interface DeleteFile {
    fun delete(file: File){
        if (file.exists())
            file.delete()
    }
}