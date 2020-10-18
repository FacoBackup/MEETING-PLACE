package br.meetingplace.interfaces.utility

import br.meetingplace.data.startup.LoginByEmail
import br.meetingplace.interfaces.file.ReadFile

interface Refresh: ReadFile {
    fun refreshData(): LoginByEmail {
        return readLoggedUser()
    }
}