package br.meetingplace.management

import br.meetingplace.data.PasswordOperations
import br.meetingplace.data.group.MemberInput
import br.meetingplace.data.user.LoginByEmail
import br.meetingplace.data.user.UserData
import br.meetingplace.entitie.User
import br.meetingplace.interfaces.file.ReadFile
import br.meetingplace.interfaces.file.WriteFile
import br.meetingplace.interfaces.utility.Generator
import br.meetingplace.interfaces.utility.Path
import br.meetingplace.interfaces.utility.Refresh
import br.meetingplace.management.interfaces.groups.Group
import br.meetingplace.management.interfaces.Login
import br.meetingplace.management.interfaces.groups.GroupChat
import br.meetingplace.management.interfaces.users.Profile
import br.meetingplace.management.interfaces.users.UserChat
import br.meetingplace.management.interfaces.thread.MainThread
import br.meetingplace.management.interfaces.thread.SubThread

class Core: ReadFile, WriteFile, Refresh, Generator, Path,Login, Profile, UserChat, MainThread, SubThread, Group, GroupChat{

    fun createUser(newUser: UserData){

        val management = readLoggedUser().email
        val user = User(newUser.realName, newUser.age, newUser.email, newUser.password)
        if(!verifyPath("users", user.getEmail()) && management == "" && user.getEmail() != ""){
            writeUser(user.getEmail(),user)
            val login = LoginByEmail(user.getEmail(), user.getPassword())
            login(login)
        }
    }//CREATE

    fun getMyProfile(): User {
        val management = readLoggedUser().email
        val nullUser = User("", -1, "", "")
        return if(verifyPath("users",management))
            readUser(management)
        else nullUser
    }//READ

    fun deleteUser(operation: PasswordOperations){

        val management = readLoggedUser().email
        val cachedPass = readLoggedUser().password
        if(management !=  "" && operation.pass == cachedPass && verifyPath("users",management)){
            val user = readUser(management)

            val followers = user.social.getFollowers()
            val following = user.social.getFollowing()

            for(i in 0 until followers.size){
                if(verifyPath("users", followers[i])) {
                    val userFollower = readUser(followers[i])
                    userFollower.social.updateFollowing(management, true)
                    writeUser(userFollower.getEmail(), userFollower)
                }
            }

            for(i in 0 until following.size){

                if(verifyPath("users", following[i])){
                    val userFollowing = readUser(following[i])
                    userFollowing.social.updateFollowers(management,true)
                    writeUser(userFollowing.getEmail(),userFollowing)
                }
            }
        /*
            for(i in 0 until groupList.size){
                member = UserMember(management,groupList[i].getEmail())
                removeMember(member) // should use an override here
            }

         */
            deleteAllThreadsFromUserId()
            logout()
        }
    }//DELETE
}