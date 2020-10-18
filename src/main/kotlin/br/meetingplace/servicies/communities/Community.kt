package br.meetingplace.servicies.communities

import br.meetingplace.data.group.Member

class Community{
    //LIKE GROUPS BUT HAS THREADS INSTEAD OF A CHAT AND ALL OF THE THREADS GOES TO THE MEMBERS TIMELINE
    private var creator = ""
    private var id = ""
    private var name = ""
    private var about= ""
    private val members = mutableListOf<Member>()
    private val threads = mutableListOf<String>()
}