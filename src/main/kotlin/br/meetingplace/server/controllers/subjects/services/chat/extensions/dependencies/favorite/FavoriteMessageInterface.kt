package br.meetingplace.server.controllers.subjects.services.chat.extensions.dependencies.favorite

import br.meetingplace.server.dto.chat.ChatSimpleOperator

interface FavoriteMessageInterface {
    fun userFavoriteMessage(data: ChatSimpleOperator)

    fun groupFavoriteMessage(data: ChatSimpleOperator)

}