package br.meetingplace.servicies.community.subClasses

class CommunityThreads private constructor() {
    companion object{
        private val obj = CommunityThreads()
        fun getObj() = obj
    }
}