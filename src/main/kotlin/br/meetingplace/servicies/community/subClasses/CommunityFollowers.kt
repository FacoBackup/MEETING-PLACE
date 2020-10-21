package br.meetingplace.servicies.community.subClasses

class CommunityFollowers private constructor(){
    companion object{
        private val obj = CommunityFollowers()
        fun getObj() = obj
    }
}