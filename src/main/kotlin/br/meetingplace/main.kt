package br.meetingplace

import br.meetingplace.data.chat.ChatComplexOperations
import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.data.chat.ChatOperations
import br.meetingplace.data.community.CommunityData
import br.meetingplace.data.group.GroupData
import br.meetingplace.data.group.GroupOperationsData
import br.meetingplace.data.group.MemberInput
import br.meetingplace.data.threads.ThreadData
import br.meetingplace.data.threads.ThreadOperationsData
import br.meetingplace.data.Data
import br.meetingplace.data.community.ApprovalData
import br.meetingplace.data.user.LoginByEmail
import br.meetingplace.data.user.SocialProfileData
import br.meetingplace.data.user.UserData
import br.meetingplace.management.Login
import br.meetingplace.management.chat.ChatFactory
import br.meetingplace.management.community.CommunityOperations
import br.meetingplace.management.thread.ThreadFactory
import br.meetingplace.management.user.UserFactory
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

val userSystem= UserFactory()
val threadSystem=  ThreadFactory()
val chatSystem = ChatFactory() // controls chat and groups
val login = Login.getLoginSystem()
val communitySystem = CommunityOperations()
fun main (){

    embeddedServer(Netty, 3000) {
        routing {
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                }
            }
            get("/communities"){
                call.respond(communitySystem.communitiesIFollow())
            }
            get("/moderator"){
                call.respond(communitySystem.moderatorIn())
            }
            post("/community/create"){
                val data = call.receive<CommunityData>()
                call.respond(communitySystem.create(data))
            }
            post("/community/follow"){
                val data = call.receive<Data>()
                call.respond(communitySystem.follow(data))
            }
            post("/community/unfollow"){
                val data = call.receive<Data>()
                call.respond(communitySystem.unfollow(data))
            }
            post("/community/approve/group"){
                val data = call.receive<ApprovalData>()
                call.respond(communitySystem.approveGroup(data))
            }
            post("/community/approve/thread"){
                val data = call.receive<ApprovalData>()
                call.respond(communitySystem.approveThread(data))
            }


            get("/user"){
                call.respond(userSystem.getMyUser())
            }

            get("/logged") {
                call.respond(userSystem.readLoggedUser().email)
            }

            post("/user/clear/notifications"){
                call.respond(userSystem.clearNotifications())
            }

            post("/user/create"){
                val user = call.receive<UserData>()
                call.respond(userSystem.createUser(user))
            }

            post("/delete"){
                call.respond(userSystem.deleteUser())
            }

            post("/login"){
                val user = call.receive<LoginByEmail>()
                call.respond(login.login(user))
            }

            post("/logoff"){
                call.respond(login.logout())
            }

            post("/create/social"){
                val user = call.receive<SocialProfileData>()
                call.respond(userSystem.createSocialProfile(user))
            }
//            post("/user/create/professional"){
//                val user = call.receive<ProfessionalProfile>()
//                call.respond(core.createProfessionalProfile(user))
//            }
            post("/follow"){
                val follow = call.receive<Data>()
                call.respond(userSystem.follow(follow))
            }
            post("/unfollow"){
                val follow = call.receive<Data>()
                call.respond(userSystem.unfollow(follow))
            }

            //MESSAGES USERS
            get("/my/messages") {
                call.respond(chatSystem.getMyChats())
            }
            post("/message/send"){
                val chat = call.receive<ChatMessage>()
                call.respond(chatSystem.sendMessage(chat))
            }
            post("/message/delete"){
                val chat = call.receive<ChatOperations>()
                call.respond(chatSystem.deleteMessage(chat))
            }
            post("/message/quote"){
                val chat = call.receive<ChatComplexOperations>()
                call.respond(chatSystem.quoteMessage(chat))
            }
            post("/message/favorite"){
                val chat = call.receive<ChatOperations>()
                call.respond(chatSystem.favoriteMessage(chat))
            }
            post("/message/unFavorite"){
                val chat = call.receive<ChatOperations>()
                call.respond(chatSystem.unFavoriteMessage(chat))
            }
            post("/message/share"){
                val chat = call.receive<ChatComplexOperations>()
                call.respond(chatSystem.shareMessage(chat))
            }
            //MESSAGES USERS

//            THREADS
            get("/user/see/threads"){
                call.respond(threadSystem.getMyThreads())
            }
            get("/user/see/timeline"){
                call.respond(threadSystem.getMyTimeline())
            }

            post("/thread/create"){
                val new = call.receive<ThreadData>()
                call.respond(threadSystem.create(new))
            }
            post("/thread/like"){
                val post = call.receive<ThreadOperationsData>()
                call.respond(threadSystem.like(post))
            }

            post("/thread/dislike"){
                val post = call.receive<ThreadOperationsData>()
                call.respond(threadSystem.dislike(post))
            }

            post("/thread/delete"){
                val thread = call.receive<ThreadOperationsData>()
                call.respond(threadSystem.delete(thread))
            }
            //THREADS

            //GROUPS
            get("/my/groups"){
                call.respond(chatSystem.readMyGroups())
            }
            get("/in/groups"){
                call.respond(chatSystem.readMemberIn())
            }
            post("/group/create"){
                val group = call.receive<GroupData>()
                call.respond(chatSystem.createGroup(group))
            }
            post("/group/member"){
                val member = call.receive<MemberInput>()
                call.respond(chatSystem.addMember(member))
            }
            post("/group/member/remove"){
                val member = call.receive<MemberInput>()
                call.respond(chatSystem.removeMember(member))
            }
            post("/group/message"){
                val chatGroup = call.receive<ChatMessage>()
                call.respond(chatSystem.sendMessage(chatGroup))
            }
            post("/group/delete/message"){
                val chatGroup = call.receive<ChatOperations>()
                call.respond(chatSystem.deleteMessage(chatGroup))
            }
            post("/group/favorite/message"){
                val chatGroup = call.receive<ChatOperations>()
                call.respond(chatSystem.favoriteMessage(chatGroup))
            }
            post("/group/unfavorite/message"){
                val chatGroup = call.receive<ChatOperations>()
                call.respond(chatSystem.unFavoriteMessage(chatGroup))
            }
            post("/group/quote/message"){
                val chatGroup = call.receive<ChatComplexOperations>()
                call.respond(chatSystem.quoteMessage(chatGroup))
            }
            post("/group/delete"){
                val group = call.receive<GroupOperationsData>()
                call.respond(chatSystem.deleteGroup(group))
            }
            //GROUPS
        }
    }.start(wait = true)
}


