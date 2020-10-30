package br.meetingplace.management.dependencies

import br.meetingplace.data.Data
import br.meetingplace.services.entitie.User
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.services.chat.Chat
import br.meetingplace.services.community.Community
import br.meetingplace.services.community.data.Report
import br.meetingplace.services.group.Group
import br.meetingplace.services.thread.MainThread

interface Verify: ReadWriteLoggedUser {

    fun verifyUser(user: User): Boolean {
        return !user.getUserName().isNullOrBlank() && user.getPassword().isNotBlank() && user.getEmail().isNotBlank() && user.getAge() >= 16
    }

    fun verifyLoggedUser(user: User): Boolean {
        val logged = readLoggedUser()

        return !user.getUserName().isNullOrBlank() && logged.email.isNotBlank() && logged.password.isNotBlank()&& logged.email == user.getEmail() && logged.password == user.getPassword()  && user.getAge() >= 16
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
            if(logged in external.getFollowers())
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