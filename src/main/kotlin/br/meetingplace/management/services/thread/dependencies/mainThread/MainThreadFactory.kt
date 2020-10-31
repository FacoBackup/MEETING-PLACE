package br.meetingplace.management.services.thread.dependencies.mainThread

import br.meetingplace.data.threads.ThreadData
import br.meetingplace.data.threads.ThreadOperationsData
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.verify.dependencies.Verify
import br.meetingplace.management.dependencies.ReadWriteController
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.readwrite.dependencies.thread.ReadWriteThread
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteUser
import br.meetingplace.management.services.thread.dependencies.ThreadFactoryInterface
import br.meetingplace.services.thread.MainThread

class MainThreadFactory private constructor(): ThreadFactoryInterface, Verify, ReadWriteLoggedUser, ReadWriteUser, ReadWriteThread, IDsController {

    companion object{
        private val Class = MainThreadFactory()
        fun getThreadFactory() = Class
    }

    override fun create(data: ThreadData): String?{
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        lateinit var thread: MainThread
        if(verifyLoggedUser(user)){
            thread = MainThread()
            //the verifyLoggedUser method insures that the userName is not null so don't mind the !!
            thread.startThread(data,generateId(), user.getUserName()!!, loggedUser)

            if(!data.idCommunity.isNullOrBlank())
                thread.updateCommunity(data.idCommunity)

            writeThread(thread, thread.getId())
            user.updateMyThreads(thread.getId(),true)
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

            ReadWriteController.getDeleteFileOperator().deleteThread(thread)
            user.updateMyThreads(data.idThread,false) //FALSE IS TO REMOVE THREAD
            writeUser(user, user.getEmail())
        }
    }//DELETE

}