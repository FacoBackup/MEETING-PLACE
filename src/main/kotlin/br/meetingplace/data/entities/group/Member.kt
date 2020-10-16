package br.meetingplace.data.entities.group

data class Member(val userId: String, val role: Int ){}
//ROLE : 0 == Normal member | 1 == Admin