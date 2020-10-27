package br.meetingplace.services.community

import br.meetingplace.services.community.communityServices.CommunityGroups
import br.meetingplace.services.community.communityServices.CommunityThreads
import br.meetingplace.services.thread.MainThread

class Community private constructor(){

    companion object{
        private val op = Community()
        fun getCommunity ()= op
    }
    private var name= "" // THE NAME IS THE IDENTIFIER
    private var id = ""
    private var about= ""
    private val followers = mutableListOf<String>()
    private val moderators = mutableListOf<String>()
    val threads = CommunityThreads.getThreads()
    val groups = CommunityGroups.getGroups()

    //GETTERS
    fun getFollowers () = followers
    fun getName() = name
    fun getId() = id
    fun getAbout() = about
    fun getModerators() = moderators
    //GETTERS

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
}