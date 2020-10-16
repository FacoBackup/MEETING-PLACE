package br.meetingplace.management.operations.finders

import br.meetingplace.management.GeneralManagement
import br.meetingplace.management.operations.ReadWrite.ReadWrite
import br.meetingplace.servicies.conversationThread.MainThread

class ServiceFinder {

    fun getThreadIndex(threadId: Int): Int {
        val management = GeneralManagement.getLoggedUser()

        return if(management != ""){

            //READING
            val rw = ReadWrite.getRW()
            val threadList = mutableListOf<MainThread>()

            repeat(threadList.size) {
                rw.readThread()
            }
            //READING

            for (i in 0 until threadList.size){
                if(threadList[i].getId() == threadId )
                    return i
            }
            -1
        }
        else -1
    }
}