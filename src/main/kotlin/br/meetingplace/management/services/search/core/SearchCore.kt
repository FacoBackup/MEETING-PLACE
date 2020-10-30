package br.meetingplace.management.services.search.core

import br.meetingplace.data.Data
import br.meetingplace.management.services.search.dependecies.community.CommunitySearch
import br.meetingplace.management.services.search.dependecies.community.CommunitySearchInterface
import br.meetingplace.management.services.search.dependecies.user.UserSearch
import br.meetingplace.management.services.search.dependecies.user.UserSearchInterface
import br.meetingplace.services.community.Community
import br.meetingplace.services.entitie.User

class SearchCore private constructor(): CommunitySearchInterface,  UserSearchInterface {

    private val community = CommunitySearch.getClass()
    private val user = UserSearch.getClass()

    companion object{
        private val Class = SearchCore()
        fun getClass () = Class
    }

    override fun searchCommunity(data: Data): List<Community> {
        return community.searchCommunity(data)
    }

    override fun searchUser(data: Data): List<User> {
        return user.searchUser(data)
    }
}