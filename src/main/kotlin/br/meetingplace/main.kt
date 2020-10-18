package br.meetingplace

import br.meetingplace.data.*
import br.meetingplace.data.conversation.ChatContent
import br.meetingplace.data.conversation.ChatFullContent
import br.meetingplace.data.conversation.operations.ChatOperations
import br.meetingplace.data.threads.SubThreadContent
import br.meetingplace.data.threads.operations.SubThreadOperations
import br.meetingplace.data.threads.ThreadContent
import br.meetingplace.data.threads.operations.ThreadOperations
import br.meetingplace.data.entities.user.Follower
import br.meetingplace.data.startup.LoginByEmail
import br.meetingplace.data.startup.SocialProfileData
import br.meetingplace.data.startup.UserData
import br.meetingplace.management.GeneralManagement
import br.meetingplace.management.entities.ProfileManagement
import br.meetingplace.management.entities.UserManagement
import br.meetingplace.management.servicies.ChatManagement
import br.meetingplace.management.servicies.ThreadManagement
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


val LogSystem = GeneralManagement.getManagement()
val UserSystem = UserManagement()
val ProfileSystem = ProfileManagement.getManagement()
val ThreadSystem = ThreadManagement.getManagement()
val ChatSystem = ChatManagement.getManagement()

fun main (){

    embeddedServer(Netty, 3000) {
        routing {
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                }
            }

            get("/user"){
                call.respond(UserSystem.getMyProfile())
            }

            get("/logged") {
                call.respond(UserSystem.getLoggedUser())
            }

            post("/user/create"){
                val user = call.receive<UserData>()
                call.respond(UserSystem.createUser(user))
            }

            post("/user/delete"){
                val user = call.receive<PasswordOperations>()
                call.respond(UserSystem.deleteUser(user))
            }

            post("/user/loginId"){
                val user = call.receive<LoginByEmail>()
                call.respond(LogSystem.login(user))
            }

            post("/user/logoff"){
                call.respond(LogSystem.logoff())
            }

            post("/user/create/social"){
                val user = call.receive<SocialProfileData>()
                call.respond(ProfileSystem.createSocialProfile(user))
            }
//            post("/user/create/professional"){
//                val user = call.receive<ProfessionalProfile>()
//                call.respond(UserSystem.createProfessionalProfile(user))
//            }
            post("/user/follow"){
                val follow = call.receive<Follower>()
                call.respond(ProfileSystem.follow(follow))
            }
            post("/user/unfollow"){
                val follow = call.receive<Follower>()
                call.respond(ProfileSystem.unfollow(follow))
            }

            //MESSAGES
            get("/messages") {
                call.respond(UserSystem.getMyChats())
            }
            post("/user/message"){
                val chat = call.receive<ChatContent>()
                call.respond(ChatSystem.sendMessage(chat))
            }
            post("/user/delete/message"){
                val chat = call.receive<ChatOperations>()
                call.respond(ChatSystem.deleteMessage(chat))
            }
            post("/user/quote/message"){
                val chat = call.receive<ChatFullContent>()
                call.respond(ChatSystem.quoteMessage(chat))
            }
            post("/user/favorite/message"){
                val chat = call.receive<ChatOperations>()
                call.respond(ChatSystem.favoriteMessage(chat))
            }
            post("/user/unFavorite/message"){
                val chat = call.receive<ChatOperations>()
                call.respond(ChatSystem.unFavoriteMessage(chat))
            }
            post("/user/share/message"){
                val chat = call.receive<ChatFullContent>()
                call.respond(ChatSystem.shareMessage(chat))
            }
            //MESSAGES

//            THREADS
            get("/user/see/threads"){
                call.respond(UserSystem.getMyThreads())
            }
            get("/user/see/timeline"){
                call.respond(UserSystem.getMyTimeline())
            }

            post("/user/thread"){
                val new = call.receive<ThreadContent>()
                call.respond(ThreadSystem.createMainThread(new))
            }
            post("/user/subthread"){
                val content = call.receive<SubThreadContent>()
                call.respond(ThreadSystem.createSubThread(content))
            }
            post("/user/delete/subthread"){
                val content = call.receive<SubThreadOperations>()
                call.respond(ThreadSystem.deleteSubThread(content))
            }
            post("/user/like"){
                val post = call.receive<ThreadOperations>()
                call.respond(ThreadSystem.likeThread(post))
            }

            post("/user/dislike"){
                val post = call.receive<ThreadOperations>()
                call.respond(ThreadSystem.dislikeThread(post))
            }

            post("/user/like/sub"){
                val post = call.receive<SubThreadOperations>()
                call.respond(ThreadSystem.likeSubThread(post))
            }
            post("/user/dislike/sub"){
                val post = call.receive<SubThreadOperations>()
                call.respond(ThreadSystem.dislikeSubThread(post))
            }
            post("/user/delete/thread"){
                val thread = call.receive<ThreadOperations>()
                call.respond(ThreadSystem.deleteThread(thread))
            }
            //THREADS

            /*
            //GROUPS
            get("/group"){
                call.respond(UserSystem.getGroups())
            }
            post("/group/create"){
                val group = call.receive<Group>()
                call.respond(UserSystem.createGroup(group))
            }
            post("/group/member"){
                val member = call.receive<UserMember>()
                call.respond(UserSystem.addMember(member))
            }
            post("/group/member/remove"){
                val member = call.receive<UserMember>()
                call.respond(UserSystem.removeMember(member))
            }
            post("/group/message"){
                val chatGroup = call.receive<GroupChatContent>()
                call.respond(UserSystem.messengerGroup(chatGroup))
            }
            //GROUPS

             */
        }
    }.start(wait = true)
}


