package br.meetingplace.management.services.chat.dependencies

import br.meetingplace.data.chat.ChatComplexOperations
import br.meetingplace.data.chat.ChatOperations

interface ChatFeaturesInterface {
    fun favoriteMessage(data: ChatOperations)
    fun unFavoriteMessage(data: ChatOperations)
    fun quoteMessage(data: ChatComplexOperations)
    fun shareMessage(data: ChatComplexOperations)
}