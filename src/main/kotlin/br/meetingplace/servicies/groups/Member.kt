package br.meetingplace.servicies.groups

data class Member(val userEmail: String, val role: Int ){}
//ROLE : 0 == Normal member | 1 == Admin