package br.meetingplace.server.controllers.subjects.services.chat.extensions.controller

import br.meetingplace.server.controllers.subjects.services.chat.dependencies.extensions.dependencies.disfavor.DisfavorMessage
import br.meetingplace.server.controllers.subjects.services.chat.dependencies.extensions.dependencies.favorite.FavoriteMessage
import br.meetingplace.server.controllers.subjects.services.chat.dependencies.extensions.dependencies.quote.QuoteMessage
import br.meetingplace.server.controllers.subjects.services.chat.dependencies.extensions.dependencies.share.ShareMessage
import br.meetingplace.server.dto.chat.ChatComplexOperator
import br.meetingplace.server.dto.chat.ChatSimpleOperator

class ChatExtensionsController private constructor() : ChatExtensionsControllerInterface {
    companion object {
        private val Class = ChatExtensionsController()
        fun getClass() = Class
    }

    private val share = ShareMessage.getClass()
    private val quote = QuoteMessage.getClass()
    private val favorite = FavoriteMessage.getClass()
    private val disfavor = DisfavorMessage.getClass()

    override fun favoriteMessage(data: ChatSimpleOperator) {
        when (data.receiver.userGroup || data.receiver.communityGroup) {
            true -> favorite.groupFavoriteMessage(data)
            false -> favorite.userFavoriteMessage(data)
        }
    }

    override fun quoteMessage(data: ChatComplexOperator) {
        when (data.receiver.userGroup || data.receiver.communityGroup) {
            true -> quote.groupQuoteMessage(data)
            false -> quote.userQuoteMessage(data)
        }
    }

    override fun shareMessage(data: ChatComplexOperator) {
        when (data.receiver.userGroup || data.receiver.communityGroup) {
            true -> share.groupShareMessage(data)
            false -> share.userShareMessage(data)
        }
    }

    override fun disfavorMessagr(data: ChatSimpleOperator) {
        when (data.receiver.userGroup || data.receiver.communityGroup) {
            true -> disfavor.groupDisfavorMessage(data)
            false -> disfavor.userDisfavorMessage(data)
        }
    }
}