package br.meetingplace.management.services.thread.dependencies.subThread

import br.meetingplace.data.threads.ThreadData
import br.meetingplace.data.threads.ThreadOperationsData
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.management.services.thread.dependencies.ThreadFactoryInterface
import br.meetingplace.services.thread.MainThread
import br.meetingplace.services.thread.SubThread

class SubThreadFactory private constructor(): ThreadFactoryInterface{
    private val iDs = IDsController.getClass()
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()

    companion object{
        private val Class = SubThreadFactory()
        fun getThreadFactory() = Class
    }

    override fun create(data: ThreadData): String?{
        val loggedUser =rw.readLoggedUser().email
        val user = rw.readUser(loggedUser)
        lateinit var subThread: SubThread
        lateinit var thread: MainThread

        if(verify.verifyUser(user)){
            thread = rw.readThread(data.idThread!!)
            if(thread.getId() != ""){
                //the verifyLoggedUser method insures that the userName is not null so don't mind the !!
                subThread = SubThread(mutableListOf(),mutableListOf(), loggedUser, data.title, data.body, user.getUserName()!!, iDs.generateId())

                thread.addSubThread(subThread)
                rw.writeThread(thread, thread.getId())
            }
        }
        return null
    }// CREATE

    override fun delete(data: ThreadOperationsData){
        val loggedUser = rw.readLoggedUser().email
        val user = rw.readUser(loggedUser)
        val thread = rw.readThread(data.idThread)

        if(verify.verifyUser(user) && thread.getId() != "" && data.idSubThread != null){
            thread.removeSubThread(data.idSubThread,loggedUser)
            rw.writeThread(thread,thread.getId())
        }
    }//DELETE

}