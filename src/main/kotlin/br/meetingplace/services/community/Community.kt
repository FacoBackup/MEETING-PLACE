package br.meetingplace.services.community

import br.meetingplace.services.community.services.CommunityServices
import br.meetingplace.services.entitie.profiles.followdata.FollowData

class Community private constructor(): CommunityServices(){

    companion object{
        private val op = Community()
        fun getCommunity ()= op
    }

    private var name: String?= null
    private var id: String?= null
    private var about: String?= null
    private val followers = mutableListOf<FollowData>()
    private val moderators = mutableListOf<FollowData>()

    fun getFollowers () = followers
    fun getName() = name
    fun getId() = id
    fun getAbout() = about
    fun getModerators() = moderators


    fun startCommunity(name: String, id: String, about: String, creator: FollowData){
        if(this.name.isNullOrBlank() && moderators.isEmpty()){
            this.name = name
            this.about = about
            this.id = id
            moderators.add(creator)
        }
    }

    fun updateModerator(data: FollowData, requester: FollowData, remove: Boolean?){
        if(requester in moderators){
            when(remove){
                true->{ //REMOVE
                    if(data in moderators)
                        moderators.remove(data)
                }
                false->{ //ADD
                    if(data !in moderators)
                        moderators.add(data)
                }
                null->{ //STEP-DOWN
                    if(data in moderators){
                        moderators.remove(data)
                        updateFollower(data, false)
                    }
                }
            }
        }
    }

    fun updateFollower(data: FollowData, remove:Boolean){
        when (remove){
            true->{
                if(data in followers){
                    followers.remove(data)
                }
            }
            false->{
                if(data !in followers){
                    followers.add(data)
                }
            }
        }
    }
}