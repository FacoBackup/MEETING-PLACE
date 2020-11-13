package br.meetingplace.server.dto

data class MemberOperator(val memberEmail: String, val stepDown: Boolean?, val identifier: Identifier, val login: Login)