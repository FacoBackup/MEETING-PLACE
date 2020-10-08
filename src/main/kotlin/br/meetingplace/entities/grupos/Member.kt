package br.meetingplace.entities.grupos

data class Member(val user: Int, val role : Int ): Group(){}
//ROLE : 0 == Normal member | 1 == Admin