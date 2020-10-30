package br.meetingplace.management.services.user.dependencies.reader

import br.meetingplace.services.entitie.User
import br.meetingplace.services.thread.MainThread

interface UserReaderInterface {
    fun getMyUser(): User
    fun getMyThreads(): List<MainThread>
    fun getMyTimeline(): List<MainThread>
}