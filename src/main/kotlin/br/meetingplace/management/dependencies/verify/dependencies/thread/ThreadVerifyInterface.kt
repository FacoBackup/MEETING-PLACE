package br.meetingplace.management.dependencies.verify.dependencies.thread

import br.meetingplace.services.thread.MainThread

interface ThreadVerifyInterface {

    fun verifyThread(thread: MainThread): Boolean
}