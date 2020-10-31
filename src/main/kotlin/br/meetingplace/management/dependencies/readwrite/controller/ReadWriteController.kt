package br.meetingplace.management.dependencies.readwrite.controller

import br.meetingplace.data.user.LoginByEmail
import br.meetingplace.management.dependencies.readwrite.dependencies.chat.ChatRW
import br.meetingplace.management.dependencies.readwrite.dependencies.chat.ReadWriteChat
import br.meetingplace.management.dependencies.readwrite.dependencies.community.CommunityRW
import br.meetingplace.management.dependencies.readwrite.dependencies.community.ReadWriteCommunity
import br.meetingplace.management.dependencies.readwrite.dependencies.community.ReadWriteReport
import br.meetingplace.management.dependencies.readwrite.dependencies.group.GroupRW
import br.meetingplace.management.dependencies.readwrite.dependencies.group.ReadWriteGroup
import br.meetingplace.management.dependencies.readwrite.dependencies.thread.ReadWriteThread
import br.meetingplace.management.dependencies.readwrite.dependencies.thread.ThreadRW
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteUser
import br.meetingplace.management.dependencies.readwrite.dependencies.user.UserRW
import br.meetingplace.services.chat.Chat
import br.meetingplace.services.community.Community
import br.meetingplace.services.community.data.Report
import br.meetingplace.services.entitie.User
import br.meetingplace.services.group.Group
import br.meetingplace.services.thread.MainThread

class ReadWriteController private constructor(): ReadWriteGroup, ReadWriteCommunity, ReadWriteUser, ReadWriteThread, ReadWriteChat, ReadWriteLoggedUser, ReadWriteReport{

    private val chat = ChatRW.getClass()
    private val group = GroupRW.getClass()
    private val community = CommunityRW.getClass()
    private val user = UserRW.getClass()
    private val thread = ThreadRW.getClass()

    companion object{
        private val Class = ReadWriteController()
        fun getClass () = Class
    }

    //USER
    override fun deleteUser(data: User) {
        user.deleteUser(data)
    }
    override fun readLoggedUser(): LoginByEmail {
        return user.readLoggedUser()
    }
    override fun readUser(fileName: String): User {
        return user.readUser(fileName)
    }
    override fun writeLoggedUser(data: LoginByEmail) {
        user.writeLoggedUser(data)
    }
    override fun writeUserToFile(data: User, fileName: String) {
        user.writeUserToFile(data, fileName)
    }

    //CHAT
    override fun deleteChat(data: Chat) {
        chat.deleteChat(data)
    }
    override fun readChat(fileName: String): Chat {
        return chat.readChat(fileName)
    }
    override fun writeChat(data: Chat, fileName: String) {
        chat.writeChat(data, fileName)
    }

    //COMMUNITY
    override fun deleteCommunity(data: Community) {
        community.deleteCommunity(data)
    }
    override fun readCommunity(fileName: String): Community {
        return community.readCommunity(fileName)
    }
    override fun deleteReport(data: Report) {
        community.deleteReport(data)
    }
    override fun readReport(fileName: String): Report {
        return community.readReport(fileName)
    }
    override fun writeCommunity(data: Community, fileName: String) {
        community.writeCommunity(data, fileName)
    }
    override fun writeReport(data: Report, fileName: String) {
        community.writeReport(data, fileName)
    }


    //GROUP
    override fun deleteGroup(data: Group) {
        group.deleteGroup(data)
    }
    override fun readGroup(fileName: String): Group {
        return group.readGroup(fileName)
    }
    override fun writeGroup(data: Group, fileName: String) {
        group.writeGroup(data, fileName)
    }


    //THREAD
    override fun deleteThread(data: MainThread) {
        thread.deleteThread(data)
    }
    override fun readThread(fileName: String): MainThread {
        return thread.readThread(fileName)
    }
    override fun writeThread(data: MainThread, fileName: String) {
        thread.writeThread(data, fileName)
    }
}