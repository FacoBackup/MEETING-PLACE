package br.meetingplace.management.interfaces

import br.meetingplace.management.interfaces.file.ReadFile
import br.meetingplace.management.interfaces.utility.Generator
import br.meetingplace.management.interfaces.utility.Verifiers

interface ConditionsVerifiers: ReadFile, Verifiers, Generator {

    fun groupChatConditions(group: String, user: String): Boolean {
        if(user != "" && verifyPath("users", user) && verifyUserSocialProfile() && verifyPath("groups", group) ){
            val groupObject = readGroup(group)
            if(groupObject.verifyMember(user))
                return true
            return false
        }
        else return false
    }

    fun usersConditions(user: String, user2: String): Boolean {
        return user != "" &&  user2 != "" && verifyPath("users", user) && verifyPath("users", user2) && verifyUserSocialProfile()
    }
}