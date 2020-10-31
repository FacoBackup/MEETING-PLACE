package br.meetingplace.management.dependencies.verify.dependencies.thread

import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.services.thread.MainThread

class ThreadVerify private constructor(): ThreadVerifyInterface{
    companion object{
        private val Class= ThreadVerify()
        fun getClass ()= Class
    }


    override fun verifyThread(thread: MainThread): Boolean {
        return thread.getId().isNotBlank()
    }
}