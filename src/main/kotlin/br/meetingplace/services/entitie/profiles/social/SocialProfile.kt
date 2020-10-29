package br.meetingplace.services.entitie.profiles.social

import br.meetingplace.services.notification.Inbox

class SocialProfile(
    private var userName: String,
    private var gender: String,
    private var nationality: String,
    private var about: String){

    private var moderatorIn = mutableListOf<String>()
    private var communitiesIFollow = mutableListOf<String>()
    private var myThreads = mutableListOf<String>()
    private var myChats = mutableListOf<String>()
    private var myGroups = mutableListOf<String>()
    private var memberIn = mutableListOf<String>()
    private var followers = mutableListOf<String>()
    private var following = mutableListOf<String>()
    private var inbox = mutableListOf<Inbox>()

    fun clearNotifications(){
        inbox.clear()
    }
    fun updateMyGroups(idGroup: String, delete: Boolean){
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
    fun updateMemberIn(idGroup: String, leave: Boolean){
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
    fun updateFollowers(userEmail: String, remove: Boolean){
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
    fun updateFollowing(userEmail: String, remove: Boolean){
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
    fun updateAbout(newAbout: String){
        about = newAbout
    }
    fun updateInbox(notification: Inbox){
        inbox.add(notification)
    }
    fun updateMyChats (id: String) {
        if (id !in myChats)
            myChats.add(id)
    }
    fun updateMyThreads (id: String, add: Boolean) {
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

    fun updateModeratorIn(id: String,leave:Boolean){
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

    fun updateCommunitiesIFollow(id: String,unfollow:Boolean){
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
    fun getModeratorIn() = moderatorIn
    fun getCommunitiesIFollow() = communitiesIFollow
    fun getMyGroups () = myGroups
    fun getMemberIn() = memberIn
    fun getFollowing() = following
    fun getFollowers() = followers
    fun getMyChats () = myChats
    fun getMyThreads () = myThreads
    fun getUserName() = userName
    //GETTERS
}