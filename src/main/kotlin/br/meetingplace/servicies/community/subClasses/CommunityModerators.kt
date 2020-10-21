package br.meetingplace.servicies.community.subClasses

class CommunityModerators private constructor(){
    companion object{
        private val obj = CommunityModerators()
        fun getObj() = obj
    }
}