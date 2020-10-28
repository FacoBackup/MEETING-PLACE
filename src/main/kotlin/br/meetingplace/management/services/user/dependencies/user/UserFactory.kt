package br.meetingplace.management.services.user.dependencies.user

import br.meetingplace.data.user.LoginByEmail
import br.meetingplace.data.user.UserData
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.DeleteFile
import br.meetingplace.services.entitie.User
import br.meetingplace.management.services.Login
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser


class UserFactory private constructor(): UserInterface, ReadWriteLoggedUser, ReadWriteUser, Verify, ReadWriteThread{

    companion object{
        private val Class = UserFactory()
        fun getFactory()= Class
    }

    override fun create(newUser: UserData){
        val logged = readLoggedUser().email
        val user = User(newUser.realName, newUser.age, newUser.email.toLowerCase(), newUser.password)

        if(logged == "" && user.getAge() > 0){
            writeUser(user,user.getEmail())
            Login.getLoginSystem().login(LoginByEmail(user.getEmail(), user.getPassword()))
        }
    }//CREATE
    override fun delete(){
        val logged = readLoggedUser().email
        val user = readUser(logged)
        lateinit var followers: MutableList<String>
        lateinit var following: MutableList<String>
        lateinit var userExternal: User

        if(verifyUser(user)){

            followers = user.social.getFollowers()
            following = user.social.getFollowing()

            for(i in 0 until followers.size){
                userExternal = readUser(followers[i])
                if(userExternal.getAge() != -1) {
                    userExternal.social.updateFollowing(logged, true)
                    writeUser(userExternal, userExternal.getEmail())
                }
            }

            for(i in 0 until following.size){
                userExternal = readUser(following[i])
                if(userExternal.getAge() != -1){
                    userExternal.social.updateFollowers(logged,true)
                    writeUser(userExternal,userExternal.getEmail())
                }
            }
            /*
                for(i in 0 until groupList.size){
                    member = UserMember(management,groupList[i].getEmail())
                    removeMember(member) // should use an override here
                }
             */
            deleteAllThreadsFromUserId()
            Login.getLoginSystem().logout()
        }
    }//DELETE

    private fun deleteAllThreadsFromUserId(){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)

        if(verifyLoggedUser(user)){
            val myThreads = user.social.getMyThreads()
            for(i in 0 until myThreads.size){
                val thread = readThread(myThreads[i])
                if(verifyThread(thread))
                    DeleteFile.getDeleteFileOperator().deleteThread(thread)
            }
        }
    } //DELETE
}