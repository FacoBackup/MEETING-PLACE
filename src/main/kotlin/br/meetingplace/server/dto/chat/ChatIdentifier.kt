package br.meetingplace.server.dto.chat

data class ChatIdentifier(val chatID: String, val mainOwnerID: String, val receiverID: String, val communityGroup: Boolean, val userGroup: Boolean)