package br.meetingplace.services.entitie.profiles.social

import br.meetingplace.services.entitie.profiles.social.interfaces.*
import br.meetingplace.services.notification.Inbox

class SocialProfile private constructor(): SocialChats, SocialCommunities, SocialFollowers, SocialGroups, SocialThreads,SocialOperators{

    companion object{
        private val Class = SocialProfile ()
        fun getClass () = Class
    }

    private var userName: String? = null
    private var gender: String? = null
    private var nationality: String? = null
    private var about: String? = null

    private var moderatorIn = mutableListOf<String>()
    private var communitiesIFollow = mutableListOf<String>()
    private var myThreads = mutableListOf<String>()
    private var myChats = mutableListOf<String>()
    private var myGroups = mutableListOf<String>()
    private var memberIn = mutableListOf<String>()
    private var followers = mutableListOf<String>()
    private var following = mutableListOf<String>()
    private var inbox = mutableListOf<Inbox>()

    override fun createSocialProfile(userName: String, about: String, nationality: String, gender: String){
        if (this.userName.isNullOrBlank()){

            this.userName = userName
            this.about = about
            this.nationality = nationality
            this.gender = gender
        }
    }

    override fun clearNotifications(){
        inbox.clear()
    }

    override fun updateMyGroups(idGroup: String, delete: Boolean){
        when (delete){
            true->{
                if(idGroup in myGroups)
                    myGroups.remove(idGroup)
            }
            false->{
                if(idGroup !in myGroups)
                    myGroups.add(idGroup)
            }
        }
    }
    override fun updateMemberIn(idGroup: String, leave: Boolean){
        when (leave){
            true->{
                if(idGroup in memberIn)
                    memberIn.remove(idGroup)
            }
            false->{
                if(idGroup !in memberIn)
                    memberIn.add(idGroup)
            }
        }
    }
    override fun updateFollowers(userEmail: String, remove: Boolean){
        when (remove){
            true ->{
                if (userEmail in followers)
                    followers.remove(userEmail)
            }
            false->{
                if (userEmail !in followers)
                    followers.add(userEmail)
            }
        }

    }
    override fun updateFollowing(userEmail: String, remove: Boolean){
        when (remove){
            true ->{
                if (userEmail in following)
                    following.remove(userEmail)
            }
            false->{
                if (userEmail !in following)
                    following.add(userEmail)
            }
        }
    }
    override fun updateAbout(newAbout: String){
        about = newAbout
    }
    override fun updateInbox(notification: Inbox){
        inbox.add(notification)
    }
    override fun updateMyChats (id: String) {
        if (id !in myChats)
            myChats.add(id)
    }
    override fun updateMyThreads (id: String, add: Boolean) {
        when(add){
            true-> {
                if (id !in myThreads)
                    myThreads.add(id)
            }
            false->{
                if (id in myThreads)
                    myThreads.remove(id)
            }
        }

    }

    override fun updateModeratorIn(id: String,leave:Boolean){
        when(leave){
            true->{
                if(id in moderatorIn)
                    moderatorIn.remove(id)
            }
            false->{
                if(id !in moderatorIn)
                    moderatorIn.add(id)
            }
        }
    }

    override fun updateCommunitiesIFollow(id: String,unfollow:Boolean){
        when(unfollow){
            true->{
                if(id in communitiesIFollow)
                    communitiesIFollow.remove(id)
            }
            false->{
                if(id !in communitiesIFollow)
                    communitiesIFollow.add(id)
            }
        }
    }

    //GETTERS
    override fun getModeratorIn() = moderatorIn
    override fun getCommunitiesIFollow() = communitiesIFollow
    override fun getMyGroups () = myGroups
    override fun getMemberIn() = memberIn
    override fun getFollowing() = following
    override fun getFollowers() = followers
    override fun getMyChats () = myChats
    override fun getMyThreads () = myThreads
    override fun getUserName() = userName
    //GETTERS
}