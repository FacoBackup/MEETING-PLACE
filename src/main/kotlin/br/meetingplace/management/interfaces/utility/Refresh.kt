package br.meetingplace.management.interfaces.utility

import br.meetingplace.data.user.LoginByEmail
import br.meetingplace.management.interfaces.file.ReadFile

interface Refresh: ReadFile {
    fun refreshData(): LoginByEmail {
        return readLoggedUser()
    }
}