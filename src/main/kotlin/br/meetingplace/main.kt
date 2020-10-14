package br.meetingplace

import br.meetingplace.data.*
import br.meetingplace.data.conversation.Conversation
import br.meetingplace.data.conversation.GroupConversation
import br.meetingplace.data.threads.SubThreadData
import br.meetingplace.data.threads.SubThreadOperations
import br.meetingplace.data.threads.ThreadContent
import br.meetingplace.data.threads.ThreadOperations
import br.meetingplace.data.user.Follower
import br.meetingplace.data.user.Login
import br.meetingplace.data.user.UserMember
import br.meetingplace.entities.groups.Group
import br.meetingplace.entities.user.User
import br.meetingplace.entities.user.profiles.SocialProfile
import br.meetingplace.servicies.management.UserManagement

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

val SystemV = UserManagement()

fun main (){

    embeddedServer(Netty, 8823) {
        routing {
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                }
            }

            get("/user"){
                call.respond(SystemV.getUsers())
            }

            get("/logged") {
                call.respond(SystemV.getLoggedUser())
            }

            post("/user/create"){
                val user = call.receive<User>()
                call.respond(SystemV.createUser(user))
            }

            post("/user/delete"){
                val user = call.receive<PasswordOperations>()
                call.respond(SystemV.deleteUser(user))
            }

            post("/user/login"){
                val user = call.receive<Login>()
                call.respond(SystemV.login(user))
            }

            post("/user/create/social"){
                val user = call.receive<SocialProfile>()
                call.respond(SystemV.createSocialProfile(user))
            }
    /*
            post("/user/create/professional"){
                val user = call.receive<ProfessionalProfile>()
                call.respond(SystemV.createProfessionalProfile(user))
            }
     */

            post("/user/logoff"){
                call.respond(SystemV.logoff())
            }
            post("/user/follow"){
                val follow = call.receive<Follower>()
                call.respond(SystemV.follow(follow))
            }
            post("/user/unfollow"){
                val follow = call.receive<Follower>()
                call.respond(SystemV.unfollow(follow))
            }
            post("/user/message"){
                val chat = call.receive<Conversation>()
                call.respond(SystemV.messengerUser(chat))
            }

            post("/user/thread"){
                val new = call.receive<ThreadContent>()
                call.respond(SystemV.createMainThread(new))
            }
            post("/user/subthread"){
                val content = call.receive<SubThreadData>()
                call.respond(SystemV.createSubThread(content))
            }
            get("/user/see/threads"){
                call.respond(SystemV.getMyThreads())
            }
            get("/user/see/timeline"){
                call.respond(SystemV.getMyTimeline())
            }
            post("/user/delete/subthread"){
                val content = call.receive<SubThreadOperations>()
                call.respond(SystemV.deleteSubThread(content))
            }
            post("/user/like"){
                val post = call.receive<ThreadOperations>()
                call.respond(SystemV.likeThread(post))
            }


            post("/user/dislike"){
                val post = call.receive<ThreadOperations>()
                call.respond(SystemV.dislikeThread(post))
            }

            post("/user/like/sub"){
                val post = call.receive<SubThreadOperations>()
                call.respond(SystemV.likeSubThread(post))
            }
            post("/user/dislike/sub"){
                val post = call.receive<SubThreadOperations>()
                call.respond(SystemV.dislikeSubThread(post))
            }
            post("/user/delete/thread"){
                val thread = call.receive<ThreadOperations>()
                call.respond(SystemV.deleteThread(thread))
            }
            get("/threads"){
                call.respond(SystemV.getThreads())
            }
            //Group related
            get("/group"){
                call.respond(SystemV.getGroups())
            }
            post("/group/create"){
                val group = call.receive<Group>()
                call.respond(SystemV.createGroup(group))
            }
            post("/group/member"){
                val member = call.receive<UserMember>()
                call.respond(SystemV.addMember(member))
            }
            post("/group/member/remove"){
                val member = call.receive<UserMember>()
                call.respond(SystemV.removeMember(member))
            }
            post("/group/message"){
                val chatGroup = call.receive<GroupConversation>()
                call.respond(SystemV.messengerGroup(chatGroup))
            }
            //Group related
        }
    }.start(wait = true)
}


