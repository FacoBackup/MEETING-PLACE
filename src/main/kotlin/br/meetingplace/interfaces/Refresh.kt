package br.meetingplace.interfaces

import br.meetingplace.data.startup.LoginByEmail

interface Refresh:ReadFile {
    fun refreshData(): LoginByEmail {
        return readLoggedUser()
    }
}