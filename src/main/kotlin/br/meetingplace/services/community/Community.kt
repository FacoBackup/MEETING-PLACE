package br.meetingplace.services.community

import br.meetingplace.services.community.communityServices.CommunityGroups
import br.meetingplace.services.community.communityServices.CommunityThreads
import br.meetingplace.services.thread.MainThread

class Community{
    private var name= "" // THE NAME IS THE IDENTIFIER
    private var id = ""
    private var about= ""
    private val followers = mutableListOf<String>()
    private val moderators = mutableListOf<String>()
    private val threads = CommunityThreads.getThreads()
    private val groups = CommunityGroups.getGroups()

    fun startCommunity(name: String, id: String, about: String, creator: String){
        if(this.name == "" && moderators.isEmpty()){
            this.name = name
            this.about = about
            this.id = id
            moderators.add(creator)
        }
    }

    fun updateFollower(userEmail: String, remove:Boolean){
        when (remove){
            true->{
                if(userEmail in followers){
                    followers.remove(userEmail)
                }
            }
            false->{
                if(userEmail !in followers){
                    followers.add(userEmail)
                }
            }
        }
    }

    fun updateThreadRequest(thread: MainThread, requester: String,remove: Boolean){
        when (remove){
            true->{

            }
            false-> {

            }
        }
    }
}