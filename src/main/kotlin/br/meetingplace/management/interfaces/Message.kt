package br.meetingplace.management.interfaces

interface Message {
    fun sendMessage()
    fun deleteMessage()
    fun favoriteMessage()
    fun unFavoriteMessage()
    fun quoteMessage()
    fun shareMessage()
}