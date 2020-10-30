package br.meetingplace.management.services.search.dependecies.group

class GroupSearch private constructor(): GroupSearchInterface{

    companion object{
        private val Class = GroupSearch()
        fun getClass() = Class
    }

}