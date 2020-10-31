package br.meetingplace.management.dependencies.verify.controller

import br.meetingplace.management.dependencies.verify.dependencies.chat.ChatVerify
import br.meetingplace.management.dependencies.verify.dependencies.chat.ChatVerifyInterface
import br.meetingplace.management.dependencies.verify.dependencies.community.CommunityVerify
import br.meetingplace.management.dependencies.verify.dependencies.community.CommunityVerifyInterface
import br.meetingplace.management.dependencies.verify.dependencies.group.GroupVerify
import br.meetingplace.management.dependencies.verify.dependencies.group.GroupVerifyInterface
import br.meetingplace.management.dependencies.verify.dependencies.thread.ThreadVerify
import br.meetingplace.management.dependencies.verify.dependencies.thread.ThreadVerifyInterface
import br.meetingplace.management.dependencies.verify.dependencies.user.UserVerify
import br.meetingplace.management.dependencies.verify.dependencies.user.UserVerifyInterface
import br.meetingplace.services.chat.Chat
import br.meetingplace.services.community.Community
import br.meetingplace.services.community.data.Report
import br.meetingplace.services.entitie.User
import br.meetingplace.services.group.Group
import br.meetingplace.services.thread.MainThread


class VerifyController private constructor(): ChatVerifyInterface, GroupVerifyInterface,CommunityVerifyInterface, ThreadVerifyInterface, UserVerifyInterface{

    private val chatVerifier  = ChatVerify.getClass()
    private val groupVerifier  = GroupVerify.getClass()
    private val communityVerifier = CommunityVerify.getClass()
    private val userVerifier  = UserVerify.getClass()
    private val threadVerifier  = ThreadVerify.getClass()

    companion object{
        private val Class= VerifyController()
        fun getClass ()= Class
    }

    override fun verifyChat(chat: Chat): Boolean {
        return chatVerifier.verifyChat(chat)
    }

    override fun verifyCommunity(community: Community): Boolean {
        return communityVerifier.verifyCommunity(community)
    }

    override fun verifyFollower(external: User, user: User): Boolean {
        return userVerifier.verifyFollower(external, user)
    }

    override fun verifyGroup(group: Group): Boolean {
        return groupVerifier.verifyGroup(group)
    }

    override fun verifyReport(report: Report): Boolean {
        return communityVerifier.verifyReport(report)
    }

    override fun verifyThread(thread: MainThread): Boolean {
        return threadVerifier.verifyThread(thread)
    }

    override fun verifyUser(user: User): Boolean {
        return userVerifier.verifyUser(user)
    }
}