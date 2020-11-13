package br.meetingplace.server.subjects.entities.dependencies.services.follow

import br.meetingplace.server.subjects.entities.SimplifiedUser

interface UserFollowInterface {
    fun updateFollowers(data: String, remove: Boolean)
    fun updateFollowing(data: String, remove: Boolean)
    fun getFollowing(): List<String>
    fun getFollowers(): List<String>
}