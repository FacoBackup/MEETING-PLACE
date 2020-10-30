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
        lateinit var followers: List<String>
        lateinit var following: List<String>
        lateinit var userExternal: User

        if(verifyUser(user)){

            followers = user.getFollowers()
            following = user.getFollowing()

            for(element in followers){
                userExternal = readUser(element)
                if(userExternal.getAge() != -1) {
                    userExternal.updateFollowing(logged, true)
                    writeUser(userExternal, userExternal.getEmail())
                }
            }

            for(element in following){
                userExternal = readUser(element)
                if(userExternal.getAge() != -1){
                    userExternal.updateFollowers(logged,true)
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
            val myThreads = user.getMyThreads()
            for(i in 0 until myThreads.size){
                val thread = readThread(myThreads[i])
                if(verifyThread(thread))
                    DeleteFile.getDeleteFileOperator().deleteThread(thread)
            }
        }
    } //DELETE
}