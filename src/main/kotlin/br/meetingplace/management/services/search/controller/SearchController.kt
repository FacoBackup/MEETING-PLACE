package br.meetingplace.management.services.search.controller

import br.meetingplace.data.Data
import br.meetingplace.management.services.search.dependecies.community.CommunitySearch
import br.meetingplace.management.services.search.dependecies.community.CommunitySearchInterface
import br.meetingplace.management.services.search.dependecies.user.UserSearch
import br.meetingplace.management.services.search.dependecies.user.UserSearchInterface
import br.meetingplace.services.community.Community
import br.meetingplace.services.entitie.User

class SearchController private constructor(): CommunitySearchInterface, UserSearchInterface{ //NEEDS WORK (DEPENDS ON THE DATA BASE)

    private val community = CommunitySearch.getClass()
    private val user = UserSearch.getClass()

    companion object{
        private val Class = SearchController()
        fun getClass () = Class
    }

    override fun searchCommunity(data: Data): Community?{
        return community.searchCommunity(data)
    }

    override fun searchUser(data: Data): List<User>{
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
