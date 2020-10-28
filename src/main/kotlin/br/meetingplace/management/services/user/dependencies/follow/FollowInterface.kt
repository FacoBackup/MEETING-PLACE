package br.meetingplace.management.services.user.dependencies.follow

import br.meetingplace.data.Data

interface FollowInterface {
    fun follow(data: Data){}
    fun unfollow(data: Data){}
}