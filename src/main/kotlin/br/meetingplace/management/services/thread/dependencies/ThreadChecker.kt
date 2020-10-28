package br.meetingplace.management.services.thread.dependencies

import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.services.thread.MainThread
import br.meetingplace.services.thread.SubThread

class ThreadChecker private constructor(): ReadWriteLoggedUser {

    companion object{
        private val checker = ThreadChecker()
        fun getChecker() = checker
    }
    fun checkLikeDislike(thread: MainThread): Int {// IF TRUE THE USER ALREADY LIKED OR DISLIKED THE THREAD
        val log = readLoggedUser()
        return when (log.email) {
            in thread.getLikes() // 0 ALREADY LIKED
            -> 0
            in thread.getDislikes() // 1 ALREADY DISLIKED
            -> 1
            else -> 2 // 2 hasn't DISLIKED or liked yet
        }
    }

    fun checkLikeDislike(thread: SubThread): Int { //CHECKER
        val log = readLoggedUser()
        val management = log.email
        return if(thread.id != ""){
            when (management) {
                in thread.likes // 0 ALREADY LIKED
                -> 0
                in thread.dislikes// 1 ALREADY DISLIKED
                -> 1
                else -> 2 // 2 hasn't DISLIKED or liked yet
            }
        }
        else 3 // SubThread Doesn't exist
    }
}