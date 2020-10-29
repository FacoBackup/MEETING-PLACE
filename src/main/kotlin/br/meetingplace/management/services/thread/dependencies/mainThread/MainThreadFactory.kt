package br.meetingplace.management.services.thread.dependencies.mainThread

import br.meetingplace.data.threads.ThreadData
import br.meetingplace.data.threads.ThreadOperationsData
import br.meetingplace.management.dependencies.Generator
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.DeleteFile
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.management.services.thread.dependencies.ThreadFactoryInterface
import br.meetingplace.services.thread.MainThread

class MainThreadFactory private constructor(): ThreadFactoryInterface, Verify, ReadWriteLoggedUser, ReadWriteUser, ReadWriteThread, Generator {

    companion object{
        private val Class = MainThreadFactory()
        fun getThreadFactory() = Class
    }

    override fun create(data: ThreadData): String?{
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        if(verifyLoggedUser(user)){
            val thread = MainThread()
            thread.startThread(data,generateId(), user.social.getUserName(), loggedUser)

            if(!data.idCommunity.isNullOrBlank())
                thread.updateCommunity(data.idCommunity)

            writeThread(thread, thread.getId())
            user.social.updateMyThreads(thread.getId(),true)
            writeUser(user, user.getEmail())


            return if(!data.idCommunity.isNullOrBlank())
                thread.getId()
            else null
        }
        return null
    } //CREATE

    override fun delete(data: ThreadOperationsData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val thread = readThread(data.idThread)

        if(verifyThread(thread) && verifyLoggedUser(user) && data.idSubThread == null ) {

            DeleteFile.getDeleteFileOperator().deleteThread(thread)
            user.social.updateMyThreads(data.idThread,false) //FALSE IS TO REMOVE THREAD
            writeUser(user, user.getEmail())
        }
    }//DELETE

}