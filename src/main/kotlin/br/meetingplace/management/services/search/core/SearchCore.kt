package br.meetingplace.management.services.search.core

import br.meetingplace.data.Data
import br.meetingplace.management.services.search.dependecies.community.CommunitySearch
import br.meetingplace.management.services.search.dependecies.community.CommunitySearchInterface
import br.meetingplace.management.services.search.dependecies.user.UserSearch
import br.meetingplace.management.services.search.dependecies.user.UserSearchInterface
import br.meetingplace.services.community.Community
import br.meetingplace.services.entitie.User

class SearchCore private constructor(): CommunitySearchInterface, UserSearchInterface{ //NEEDS WORK (DEPENDS ON THE DATA BASE)

    private val community = CommunitySearch.getClass()
    private val user = UserSearch.getClass()

    companion object{
        private val Class = SearchCore()
        fun getClass () = Class
    }

    override fun searchCommunity(data: Data): Community?{
        return community.searchCommunity(data)
    }

    override fun searchUser(data: Data): User?{
        return user.searchUser(data)
    }
}
//
//fun search(data: Data){
//    when(verifyType(data)){
//        SearchType.USER->{
//            return user.searchUser(data)
//        }
//        SearchType.COMMUNITY->{
//            return community.searchCommunity(data)
//        }
//    }
//}
//
//private fun verifyType(data: Data): SearchType?{
//    val asUser = readUser(data.ID)
//    val asCommunity = readCommunity(getCommunityId(data.ID))
//
//    return if(verifyUser(asUser) && !verifyCommunity(asCommunity))
//        SearchType.USER
//    else if (!verifyUser(asUser) && verifyCommunity(asCommunity))
//        SearchType.COMMUNITY
//    else null
//}