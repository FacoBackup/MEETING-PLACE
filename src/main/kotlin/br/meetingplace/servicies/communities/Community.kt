package br.meetingplace.servicies.communities

import br.meetingplace.servicies.groups.Member

class Community{
    //LIKE GROUPS BUT HAS THREADS INSTEAD OF A CHAT AND ALL OF THE THREADS GOES TO THE MEMBERS TIMELINE
    private var creator = ""
    private var name= "" // THE NAME IS THE IDENTIFIER
    private var about= ""
    private val rules = mutableListOf<String>()
    private val moderators = mutableListOf<String>()
    private val followers = mutableListOf<String>()
    private val newThreads = mutableListOf<String>()
    private val topThreads = mutableListOf<String>()
}