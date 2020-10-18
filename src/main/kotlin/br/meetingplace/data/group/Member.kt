package br.meetingplace.data.group

data class Member(val userId: String, val role: Int ){}
//ROLE : 0 == Normal member | 1 == Admin