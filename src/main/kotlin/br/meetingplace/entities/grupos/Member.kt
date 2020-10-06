package br.meetingplace.entities.grupos

class Member(User : Int, Role: Int): Group(){
    val user = User
    val role = Role //0 normal member | 1 admin member
}