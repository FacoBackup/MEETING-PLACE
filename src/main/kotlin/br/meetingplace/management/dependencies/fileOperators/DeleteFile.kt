package br.meetingplace.management.dependencies.fileOperators

import br.meetingplace.services.entitie.User
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteGroup
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.group.Group
import br.meetingplace.services.thread.MainThread
import java.io.File
import java.lang.Exception

class DeleteFile private constructor(): ReadWriteUser, ReadWriteGroup, ReadWriteThread {

    companion object{
        private val delete = DeleteFile()
        fun getDeleteFileOperator() = delete
    }

    fun deleteThread(data: MainThread){
        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/threads/${data.getId()}.json"
        val file = File(path)
        try {
            file.delete()
        }catch (e: Exception){
            println("fileNotFound")
        }
    }

    fun deleteGroup(data: Group){
        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/groups/${data.getGroupId()}.json"
        val file = File(path)
        try {
            file.delete()
        }catch (e: Exception){
            println("fileNotFound")
        }
    }
    fun deleteUser(data: User){
        val path = File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/groups/${data.getEmail()}.json"
        val file = File(path)
        try {
            file.delete()
        }catch (e: Exception){
            println("fileNotFound")
        }
    }
}