package br.meetingplace.server.controllers.subjects.entities.follow

import br.meetingplace.server.controllers.readwrite.community.CommunityRWInterface
import br.meetingplace.server.controllers.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.SimpleOperator

interface FollowInterface {
    fun follow(data: SimpleOperator, rwUser: UserRWInterface, rwCommunity: CommunityRWInterface)
    fun unfollow(data: SimpleOperator, rwUser: UserRWInterface, rwCommunity: CommunityRWInterface)
}