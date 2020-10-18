package br.meetingplace.management.entities

import br.meetingplace.data.PasswordOperations
import br.meetingplace.data.entities.group.UserMember
import br.meetingplace.data.startup.LoginById
import br.meetingplace.data.startup.UserData
import br.meetingplace.entities.user.User
import br.meetingplace.interfaces.*
import br.meetingplace.management.GeneralManagement
import br.meetingplace.management.servicies.ThreadManagement
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.conversationThread.MainThread
import java.io.File

class UserManagement: ReadFile, WriteFile, Refresh, Generator, Path{

    fun createUser(newUser: UserData){
        val log = refreshData()
        val management = log.user

        val user = User(newUser.realName, newUser.age, newUser.email, newUser.password)

        if(user.getId() == "" && management == "" && user.getEmail() != ""){
            user.startUser(generateId())
            writeUser(user.getId(),user)
            val login = LoginById(user.getId(), user.getPassword())
            GeneralManagement.getManagement().loginId(login)
        }
    }

    fun deleteUser(operation: PasswordOperations){
        val log = refreshData()
        val management = log.user
        val cachedPass = log.password



        if(management !=  "" && operation.pass == cachedPass && verifyPath("users",management)){
            val user = readUser(management)
            var member: UserMember

//            for(i in 0 until user.social.followers.size){
//                val fileFollower = File("${user.social.followers[i]}.json").exists()
//                if(fileFollower) {
//                    val following = readUser(user.social.following[i])
//                    following.social.following.remove(management)
//                    writeUser(following.getId(), following)
//                }
//            }
//
//            for(i in 0 until user.social.following.size){
//                val fileFollowing = File("${user.social.following[i]}.json").exists()
//                if(fileFollowing){
//                    val follower = readUser(user.social.following[i])
//                    follower.social.followers.remove(management)
//                    writeUser(follower.getId(),follower)
//                }
//            }
        /*
            for(i in 0 until groupList.size){
                member = UserMember(management,groupList[i].getId())
                removeMember(member) // should use an override here
            }

         */
            val threadManagement = ThreadManagement.getManagement()
            threadManagement.deleteAllThreadsFromUserId()
            GeneralManagement.getManagement().logoff()
        }
    }

    fun getMyProfile(): User {
        val log = refreshData()
        val management = log.user
        val nullUser = User("", -1, "", "")
        return if(verifyPath("users",management))
            readUser(management)
        else nullUser
    }

    fun getMyChats(): MutableList<Chat> {
        val log = refreshData()
        val management = log.user

        val chats = mutableListOf<Chat>()
        if( verifyPath("users",management)){
            val user = readUser(management)

            val userChats = user.social.getMyChats()
            for(i in 0 until userChats.size){
                if (verifyPath("chats",userChats[i])){
                    val chat = readChat(userChats[i])
                    chats.add(chat)
                }
            }
            return chats
        }
        return chats
    }

    fun getMyThreads(): MutableList<MainThread> {
        val log = refreshData()
        val management = log.user


        val myThreads = mutableListOf<MainThread>()

        if( verifyPath("users",management) && management != ""){
            val user = readUser(management)
            val myThreadsIds = user.social.getMyThreads()

            for (i in 0 until myThreadsIds.size){

                if (verifyPath("threads",myThreadsIds[i])){
                    val thread = readThread(myThreadsIds[i])
                    myThreads.add(thread)
                }
            }
            return myThreads
        }
        return myThreads
    }

    fun getMyTimeline(): MutableList<MainThread> {
        val log = refreshData()
        val management = log.user


        val myTimeline = mutableListOf<MainThread>()

        if( verifyPath("users",management) && management != "") {
            val user = readUser(management)
            val followingIds = user.social.following

            for (i in 0 until followingIds.size){
                if( verifyPath("users",followingIds[i])){

                    val following = readUser(followingIds[i])
                    val followingThreads = following.social.getMyThreads()

                    for (j in 0 until followingThreads.size){
                        if (verifyPath("threads",followingThreads[j])){
                            val thread = readThread(followingThreads[j])
                            myTimeline.add(thread)
                        }
                    }
                }
            }
            return myTimeline
        }
        return myTimeline
    }

    fun getLoggedUser()= refreshData().user
}