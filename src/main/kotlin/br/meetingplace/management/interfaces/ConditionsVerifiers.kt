package br.meetingplace.management.interfaces

import br.meetingplace.interfaces.file.ReadFile
import br.meetingplace.interfaces.utility.Generator
import br.meetingplace.interfaces.utility.Verifiers

interface ConditionsVerifiers: ReadFile, Verifiers, Generator {

    fun groupConditions(group: String, user: String): Boolean {
        if(user != "" && verifyUserSocialProfile() && verifyPath("groups", group) && verifyPath("users", user)){
            val groupObject = readGroup(group)
            if(groupObject.verifyMember(user) && groupObject.getChatId() != "" && verifyPath("chats", groupObject.getChatId()))
                return true
            return false
        }
        else return false
    }

    fun groupChatConditions(group: String, user: String): Int {
        if(user != "" && verifyUserSocialProfile() && verifyPath("groups", group) && verifyPath("users", user)){
            val groupObject = readGroup(group)
            if(groupObject.verifyMember(user) && groupObject.getChatId() != "" && verifyPath("chats", groupObject.getChatId())){
                return if(verifyPath("chats", groupObject.getChatId()))
                    1
                else 2
            }
            return 0
        }
        else return 0
    }

    fun usersConditions(user: String, user2: String): Boolean {
        return user != "" &&  user2 != "" && verifyPath("users", user) && verifyPath("users", user2) && verifyUserSocialProfile()
    }
}