package br.meetingplace.interfaces

import br.meetingplace.data.startup.LoginById

interface Refresh:ReadFile {
    fun refreshData(): LoginById {
        return readLoggedUser()
    }
}