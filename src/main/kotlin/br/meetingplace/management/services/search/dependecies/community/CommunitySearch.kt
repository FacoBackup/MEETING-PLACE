package br.meetingplace.management.services.search.dependecies.community

class CommunitySearch private constructor(): CommunitySearchInterface{

    companion object{
        private val Class = CommunitySearch()
        fun getClass() = Class
    }

}