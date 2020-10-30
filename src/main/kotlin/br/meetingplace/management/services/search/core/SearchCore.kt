package br.meetingplace.management.services.search.core

import br.meetingplace.management.services.search.dependecies.community.CommunitySearchInterface
import br.meetingplace.management.services.search.dependecies.group.GroupSearchInterface
import br.meetingplace.management.services.search.dependecies.user.UserSearchInterface

class SearchCore private constructor(): CommunitySearchInterface, GroupSearchInterface, UserSearchInterface {

    companion object{
        private val Class = SearchCore()
        fun getClass () = Class
    }


}