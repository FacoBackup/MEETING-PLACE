package br.meetingplace.services.community

import br.meetingplace.services.community.services.CommunityServices

class Community private constructor(): CommunityServices(){

    companion object{
        private val op = Community()
        fun getCommunity ()= op
    }

    private var name= "" // THE NAME IS THE IDENTIFIER
    private var id = ""
    private var about= ""
    private val followers = mutableListOf<String>()
    private val moderators = mutableListOf<String>()

    fun getFollowers () = followers
    fun getName() = name
    fun getId() = id
    fun getAbout() = about
    fun getModerators() = moderators


    fun startCommunity(name: String, id: String, about: String, creator: String){
        if(this.name == "" && moderators.isEmpty()){
            this.name = name
            this.about = about
            this.id = id
            moderators.add(creator)
        }
    }

    fun updateModerator(userEmail: String, requester: String, remove: Boolean?){
        if(requester in moderators){
            when(remove){
                true->{ //REMOVE
                    if(userEmail in moderators)
                        moderators.remove(userEmail)
                }
                false->{ //ADD
                    if(userEmail !in moderators)
                        moderators.add(userEmail)
                }
                null->{ //STEP-DOWN
                    if(userEmail in moderators){
                        moderators.remove(userEmail)
                        updateFollower(userEmail, false)
                    }
                }
            }
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