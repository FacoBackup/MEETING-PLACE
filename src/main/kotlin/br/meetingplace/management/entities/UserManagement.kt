package br.meetingplace.management.entities

import br.meetingplace.data.PasswordOperations
import br.meetingplace.data.entities.group.UserMember
import br.meetingplace.data.startup.UserData
import br.meetingplace.entities.user.User
import br.meetingplace.interfaces.Generator

import br.meetingplace.management.GeneralManagement
import br.meetingplace.management.operations.ReadWrite.ReadWrite
import br.meetingplace.management.operations.verifiers.UserVerifiers
import java.io.File

class UserManagement: ProfileManagement(), Generator{

    private val verifier = UserVerifiers.getUserVerifier()
    private val rw = ReadWrite.getRW()
    private val system = GeneralManagement.getManagement()
    private val management = system.getLoggedUser()
    private val cachedPass = system.getCachedPassword()

    fun createUser(newUser: UserData){

        val user = User(newUser.realName, newUser.age, newUser.email, newUser.password)

        if(user.getId() == "" && management == "" && user.getEmail() != ""){
            user.startUser(generateId())
            rw.writeUser(user.getId(),user)
        }
    }

    fun deleteUser(operation: PasswordOperations){
        val fileUser = File("$management.json").exists()

        if(management !=  "" && operation.pass == cachedPass && fileUser){
            val user = rw.readUser(management)
            var member: UserMember

            for(i in 0 until user.social.followers.size){
                val fileFollower = File("${user.social.followers[i]}.json").exists()
                if(fileFollower) {
                    val following = rw.readUser(user.social.following[i])
                    following.social.following.remove(management)
                    rw.writeUser(following.getId(), following)
                }
            }

            for(i in 0 until user.social.following.size){
                val fileFollowing = File("${user.social.following[i]}.json").exists()
                if(fileFollowing){
                    val follower = rw.readUser(user.social.following[i])
                    follower.social.followers.remove(management)
                    rw.writeUser(follower.getId(),follower)
                }
            }
        /*
            for(i in 0 until groupList.size){
                member = UserMember(management,groupList[i].getId())
                removeMember(member) // should use an override here
            }

         */
            deleteAllThreadsFromUserId()

            system.logoff()
        }
    }
}