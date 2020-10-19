package br.meetingplace

import br.meetingplace.data.*
import br.meetingplace.data.chat.ChatComplexOperations
import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.data.chat.ChatOperations
import br.meetingplace.data.threads.subThread.SubThreadContent
import br.meetingplace.data.threads.subThread.SubThreadOperations
import br.meetingplace.data.threads.mainThread.ThreadContent
import br.meetingplace.data.threads.mainThread.ThreadOperations
import br.meetingplace.data.user.Follower
import br.meetingplace.data.user.LoginByEmail
import br.meetingplace.data.user.SocialProfileData
import br.meetingplace.data.user.UserData
import br.meetingplace.management.Core
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

val core = Core()
fun main (){

    embeddedServer(Netty, 3000) {
        routing {
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                }
            }

            get("/user"){
                call.respond(core.getMyProfile())
            }

            get("/logged") {
                call.respond(core.readLoggedUser().email)
            }

            post("/user/create"){
                val user = call.receive<UserData>()
                call.respond(core.createUser(user))
            }

            post("/user/delete"){
                val user = call.receive<PasswordOperations>()
                call.respond(core.deleteUser(user))
            }

            post("/user/loginId"){
                val user = call.receive<LoginByEmail>()
                call.respond(core.login(user))
            }

            post("/user/logoff"){
                call.respond(core.logout())
            }

            post("/user/create/social"){
                val user = call.receive<SocialProfileData>()
                call.respond(core.createSocialProfile(user))
            }
//            post("/user/create/professional"){
//                val user = call.receive<ProfessionalProfile>()
//                call.respond(core.createProfessionalProfile(user))
//            }
            post("/user/follow"){
                val follow = call.receive<Follower>()
                call.respond(core.follow(follow))
            }
            post("/user/unfollow"){
                val follow = call.receive<Follower>()
                call.respond(core.unfollow(follow))
            }

            //MESSAGES USERS
            get("/messages") {
                call.respond(core.getMyChats())
            }
            post("/user/message"){
                val chat = call.receive<ChatMessage>()
                call.respond(core.sendMessageUser(chat))
            }
            post("/user/delete/message"){
                val chat = call.receive<ChatOperations>()
                call.respond(core.deleteMessageUser(chat))
            }
            post("/user/quote/message"){
                val chat = call.receive<ChatComplexOperations>()
                call.respond(core.quoteMessageUser(chat))
            }
            post("/user/favorite/message"){
                val chat = call.receive<ChatOperations>()
                call.respond(core.favoriteMessageUser(chat))
            }
            post("/user/unFavorite/message"){
                val chat = call.receive<ChatOperations>()
                call.respond(core.unFavoriteMessageUser(chat))
            }
            post("/user/share/message"){
                val chat = call.receive<ChatComplexOperations>()
                call.respond(core.shareMessageUser(chat))
            }
            //MESSAGES USERS

//            THREADS
            get("/user/see/threads"){
                call.respond(core.getMyThreads())
            }
            get("/user/see/timeline"){
                call.respond(core.getMyTimeline())
            }

            post("/user/thread"){
                val new = call.receive<ThreadContent>()
                call.respond(core.createMainThread(new))
            }
            post("/user/subthread"){
                val content = call.receive<SubThreadContent>()
                call.respond(core.createSubThread(content))
            }
            post("/user/delete/subthread"){
                val content = call.receive<SubThreadOperations>()
                call.respond(core.deleteSubThread(content))
            }
            post("/user/like"){
                val post = call.receive<ThreadOperations>()
                call.respond(core.likeThread(post))
            }

            post("/user/dislike"){
                val post = call.receive<ThreadOperations>()
                call.respond(core.dislikeThread(post))
            }

            post("/user/like/sub"){
                val post = call.receive<SubThreadOperations>()
                call.respond(core.likeSubThread(post))
            }
            post("/user/dislike/sub"){
                val post = call.receive<SubThreadOperations>()
                call.respond(core.dislikeSubThread(post))
            }
            post("/user/delete/thread"){
                val thread = call.receive<ThreadOperations>()
                call.respond(core.deleteThread(thread))
            }
            //THREADS

            /*
            //GROUPS
            get("/group"){
                call.respond(core.getGroups())
            }
            post("/group/create"){
                val group = call.receive<Group>()
                call.respond(core.createGroup(group))
            }
            post("/group/member"){
                val member = call.receive<UserMember>()
                call.respond(core.addMember(member))
            }
            post("/group/member/remove"){
                val member = call.receive<UserMember>()
                call.respond(core.removeMember(member))
            }
            post("/group/message"){
                val chatGroup = call.receive<GroupChatContent>()
                call.respond(core.messengerGroup(chatGroup))
            }
            //GROUPS

             */
        }
    }.start(wait = true)
}


