package br.meetingplace.servicies.community

import br.meetingplace.servicies.community.subClasses.CommunityModerators

class Community{
    private var name= "" // THE NAME IS THE IDENTIFIER
    private var about= ""
    private val moderators = CommunityModerators.getObj()

//
//    private val followers = mutableListOf<String>()
//    private val groups = mutableListOf<String>()
//    private val approvedThreads = mutableListOf<String>() //THIS ONE GOES TO THE FOLLOWER TIMELINE
//
//    //THESE ONES GOES TO THE INBOX OF THE MODERATORS UNTIL IT IS RESOLVED
//    private val toBeApprovedThreads = mutableListOf<String>()
//    private val reportedThreads = mutableListOf<Report>()
//    private val reportedFollowers = mutableListOf<Report>()
//    private val reportedGroups = mutableListOf<Report>()
//
//
//    fun startCommunity(data: CommunityData, creator: String){
//        if(name == "" && about == "" && moderators.size == 0){
//            name = data.name
//            moderators.add(creator)
//            about = data.about
//        }
//    }

}