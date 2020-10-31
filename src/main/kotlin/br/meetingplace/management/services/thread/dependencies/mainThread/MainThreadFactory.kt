package br.meetingplace.management.services.thread.dependencies.mainThread

import br.meetingplace.data.threads.ThreadData
import br.meetingplace.data.threads.ThreadOperationsData
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.management.services.thread.dependencies.ThreadFactoryInterface
import br.meetingplace.services.thread.MainThread

class MainThreadFactory private constructor(): ThreadFactoryInterface{
    private val iDs = IDsController.getClass()
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()

    companion object{
        private val Class = MainThreadFactory()
        fun getThreadFactory() = Class
    }

    override fun create(data: ThreadData): String?{
        val loggedUser = rw.readLoggedUser().email
        val user = rw.readUser(loggedUser)
        lateinit var thread: MainThread
        if(verify.verifyUser(user)){
            thread = MainThread()
            //the verifyLoggedUser method insures that the userName is not null so don't mind the !!
            thread.startThread(data,iDs.generateId(), user.getUserName(), loggedUser)

            if(!data.idCommunity.isNullOrBlank())
                thread.updateCommunity(data.idCommunity)

            rw.writeThread(thread, thread.getId())
            user.updateMyThreads(thread.getId(),true)
            rw.writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))


            return if(!data.idCommunity.isNullOrBlank())
                thread.getId()
            else null
        }
        return null
    } //CREATE

    override fun delete(data: ThreadOperationsData){
        val loggedUser = rw.readLoggedUser().email
        val user = rw.readUser(loggedUser)
        val thread = rw.readThread(data.idThread)

        if(verify.verifyThread(thread) && verify.verifyUser(user) && data.idSubThread == null ) {

            rw.deleteThread(thread)
            user.updateMyThreads(data.idThread,false) //FALSE IS TO REMOVE THREAD
            rw.writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
        }
    }//DELETE

}