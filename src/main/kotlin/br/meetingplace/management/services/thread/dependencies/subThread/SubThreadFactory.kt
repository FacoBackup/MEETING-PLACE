package br.meetingplace.management.services.thread.dependencies.subThread

import br.meetingplace.data.threads.ThreadData
import br.meetingplace.data.threads.ThreadOperationsData
import br.meetingplace.management.dependencies.Generator
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.management.services.thread.dependencies.ThreadFactoryInterface
import br.meetingplace.services.thread.MainThread
import br.meetingplace.services.thread.SubThread

class SubThreadFactory private constructor(): ThreadFactoryInterface, Verify, ReadWriteLoggedUser, ReadWriteUser, ReadWriteThread, Generator {

    companion object{
        private val Class = SubThreadFactory()
        fun getThreadFactory() = Class
    }

    override fun create(data: ThreadData): String?{
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        lateinit var subThread: SubThread
        lateinit var thread: MainThread

        if(verifyLoggedUser(user)){
            thread = readThread(data.idThread!!)
            if(thread.getId() != ""){
                //the verifyLoggedUser method insures that the userName is not null so don't mind the !!
                subThread = SubThread(mutableListOf(),mutableListOf(), loggedUser, data.title, data.body, user.getUserName()!!, generateId())

                thread.addSubThread(subThread)
                writeThread(thread, thread.getId())
            }
        }
        return null
    }// CREATE

    override fun delete(data: ThreadOperationsData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val thread = readThread(data.idThread)

        if(verifyLoggedUser(user) && thread.getId() != "" && data.idSubThread != null){
            thread.removeSubThread(data.idSubThread,loggedUser)
            writeThread(thread,thread.getId())
        }
    }//DELETE

}