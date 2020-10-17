package br.meetingplace.management.entities

import br.meetingplace.data.PasswordOperations
import br.meetingplace.data.entities.group.UserMember
import br.meetingplace.data.startup.UserData
import br.meetingplace.entities.user.User
import br.meetingplace.interfaces.Generator
import br.meetingplace.interfaces.ReadFile
import br.meetingplace.interfaces.Refresh
import br.meetingplace.interfaces.WriteFile
import br.meetingplace.management.GeneralManagement
import br.meetingplace.management.servicies.ThreadManagement
import br.meetingplace.servicies.conversationThread.MainThread
import java.io.File

class UserManagement: ReadFile, WriteFile, Refresh, Generator{

    fun createUser(newUser: UserData){
        val log = refreshData()
        val management = log.user

        val user = User(newUser.realName, newUser.age, newUser.email, newUser.password)

        if(user.getId() == "" && management == "" && user.getEmail() != ""){
            user.startUser(generateId())
            writeUser(user.getId(),user)
        }
    }

    fun deleteUser(operation: PasswordOperations){
        val log = refreshData()
        val management = log.user
        val cachedPass = log.password

        val fileUser = File("$management.json").exists()

        if(management !=  "" && operation.pass == cachedPass && fileUser){
            val user = readUser(management)
            var member: UserMember

            for(i in 0 until user.social.followers.size){
                val fileFollower = File("${user.social.followers[i]}.json").exists()
                if(fileFollower) {
                    val following = readUser(user.social.following[i])
                    following.social.following.remove(management)
                    writeUser(following.getId(), following)
                }
            }

            for(i in 0 until user.social.following.size){
                val fileFollowing = File("${user.social.following[i]}.json").exists()
                if(fileFollowing){
                    val follower = readUser(user.social.following[i])
                    follower.social.followers.remove(management)
                    writeUser(follower.getId(),follower)
                }
            }
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

        println(management)
        val fileUser = File("$management.json").exists()
        println(fileUser)
        val nullUser = User("", -1, "", "")
        return if(fileUser)
            readUser(management)
        else nullUser
    }

    fun getMyThreads(): MutableList<MainThread> {
        val log = refreshData()
        val management = log.user

        val fileUser = File("$management.json").exists()
        val myThreads = mutableListOf<MainThread>()

        if(fileUser && management != ""){
            val user = readUser(management)
            val myThreadsIds = user.social.getMyThreads()

            for (i in 0 until myThreadsIds.size){
                val fileThread = File("${myThreadsIds[i]}.json").exists()
                if (fileThread){
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

        val fileUser = File("$management.json").exists()
        val myTimeline = mutableListOf<MainThread>()

        if(fileUser && management != "") {
            val user = readUser(management)
            val followingIds = user.social.following

            for (i in 0 until followingIds.size){
                val fileFollowing = File("${followingIds[i]}.json").exists()
                if(fileFollowing){

                    val following = readUser(followingIds[i])
                    val followingThreads = following.social.getMyThreads()

                    for (j in 0 until followingThreads.size){
                        val fileThread = File("${followingThreads[j]}.json").exists()
                        if (fileThread){
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