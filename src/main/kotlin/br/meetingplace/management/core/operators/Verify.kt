package br.meetingplace.management.core.operators

import br.meetingplace.entitie.User
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.services.chat.Chat
import br.meetingplace.services.group.Group
import br.meetingplace.services.thread.MainThread

interface Verify: ReadWriteLoggedUser {
    fun verifyUser(user: User): Boolean {
        val logged = readLoggedUser()

        return logged.email != "" && logged.password != "" && logged.email == user.getEmail() && user.getAge() >= 16 && user.social.getUserName() != "" && logged.password == user.getPassword()
    }

    fun verifyThread(thread: MainThread): Boolean {
        return thread.getId() != ""
    }

    fun verifyChat(chat: Chat): Boolean {
        return chat.getConversationId() != ""
    }

    fun verifyGroup(group: Group): Boolean {
        return group.getGroupId() != ""
    }

    fun verifyFollower(external: User, user: User): Boolean {
        val logged = readLoggedUser().email

        if(user.getAge() != -1 && external.getAge() != -1 && logged != ""){
            if(logged in external.social.getFollowers())
                return true
        }
        return false
    }
}