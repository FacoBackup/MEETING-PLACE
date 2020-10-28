package br.meetingplace.management.dependencies

import br.meetingplace.entitie.User
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.services.chat.Chat
import br.meetingplace.services.community.Community
import br.meetingplace.services.community.data.Report
import br.meetingplace.services.group.Group
import br.meetingplace.services.thread.MainThread

interface Verify: ReadWriteLoggedUser {

    fun verifyUser(user: User): Boolean {
        return user.getEmail().isNotBlank() && user.getAge() >= 16 && user.social.getUserName().isNotBlank() && user.getPassword().isNotBlank()
    }

    fun verifyLoggedUser(user: User): Boolean {
        val logged = readLoggedUser()

        return logged.email.isNotBlank() && logged.password.isNotBlank()&& logged.email == user.getEmail() && user.getAge() >= 16 && user.social.getUserName().isNotBlank() && logged.password == user.getPassword()
    }


    fun verifyThread(thread: MainThread): Boolean {
        return thread.getId().isNotBlank()
    }

    fun verifyChat(chat: Chat): Boolean {
        return chat.getConversationId().isNotBlank()
    }

    fun verifyGroup(group: Group): Boolean {
        return group.getGroupId().isNotBlank()
    }

    fun verifyFollower(external: User, user: User): Boolean {
        val logged = readLoggedUser().email

        if(user.getAge() != -1 && external.getAge() != -1 && logged.isNotBlank()){
            if(logged in external.social.getFollowers())
                return true
        }
        return false
    }

    fun verifyReport(report: Report): Boolean {
        return report.reportId.isNotBlank()
    }

    fun verifyCommunity(community: Community): Boolean{
        return community.getId().isNotBlank() && community.getName().isNotBlank()
    }
}