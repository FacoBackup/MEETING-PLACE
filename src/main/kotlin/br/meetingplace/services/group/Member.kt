package br.meetingplace.services.group

data class Member(val userEmail: String, val role: Int ){}
//ROLE : 0 == Normal member | 1 == Admin