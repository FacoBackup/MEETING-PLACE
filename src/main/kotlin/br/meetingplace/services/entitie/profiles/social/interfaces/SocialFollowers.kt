package br.meetingplace.services.entitie.profiles.social.interfaces

import br.meetingplace.services.entitie.profiles.followdata.FollowData

interface SocialFollowers {
    fun updateFollowers(data: FollowData, remove: Boolean)
    fun updateFollowing(data: FollowData, remove: Boolean)
    fun getFollowing(): List<FollowData>
    fun getFollowers(): List<FollowData>
}